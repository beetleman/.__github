#!/usr/bin/env bb
;; Build script: render canonical src/ into harness-specific outputs.
;; Usage: bb scripts/build.clj <harness> [out-dir]
;;        bb scripts/build.clj --harness <harness> [--out-dir <dir>]

(ns build
  (:require [babashka.cli :as cli]
            [babashka.fs :as fs]
            [clojure.string :as str]
            [common :refer [repo-root rpath eprintln config]]
            [selmer.filters :as sf]
            [selmer.parser :as sp]
            [selmer.util :as su]))

(su/turn-off-escaping!)

;; Resolve Selmer includes (e.g. `src/common/...`) from repo-root.
(sp/set-resource-path! repo-root)

;; Fail loudly on missing template values instead of rendering empty strings.
(su/set-missing-value-formatter!
 (fn [tag _ctx]
   (throw (ex-info "Missing Selmer render value"
                   {:tag tag}))))

(defn- rel-from-root
  "Strips the repo-root prefix from an absolute path."
  [abs-path]
  (str/replace-first abs-path (str repo-root "/") ""))

;; YAML-aware quoting: leave plain scalars unquoted, double-quote ambiguous ones.

(def ^:private quote-trigger-chars
  "Characters anywhere in the string that force quoting."
  (set "#&*!|>\"%@\n"))

(def ^:private numeric-like-re
  ;; Numbers + YAML 1.1 bool/null spellings (e.g. unquoted `on` round-trips as
  ;; `true`). One optional dot only, so version strings like "1.0.0" stay unquoted.
  #"(?i)^(true|false|yes|no|on|off|y|n|null|~|-?\d+(\.\d+)?)$")

(defn- needs-quoting? [^String s]
  (cond
    (str/blank? s)                                true
    (some quote-trigger-chars s)                  true
    ;; A colon only breaks YAML when followed by whitespace (mapping indicator).
    (re-find #":(\s|$)" s)                        true
    (or (str/starts-with? s "'")
        (str/starts-with? s "`"))                 true
    (and (>= (count s) 2)
         (contains? #{\- \? \* \! \@} (first s))
         (Character/isWhitespace ^char (second s))) true
    (re-matches numeric-like-re s)                true
    :else                                         false))

(defn- quote-scalar
  "Double-quotes a scalar, escaping backslashes and quotes."
  [^String s]
  (str \"
       (-> s
           (str/replace "\\" "\\\\")
           (str/replace "\"" "\\\""))
       \"))

(defn yaml-scalar
  "Selmer filter. Returns Selmer's [:safe s] vector to tell Selmer
  the string is already escape-safe and must not be HTML-escaped."
  [v]
  (let [s (str v)]
    [:safe (if (needs-quoting? s) (quote-scalar s) s)]))

;; Templates use this as `{{x|yaml-scalar}}`.
(sf/add-filter! :yaml-scalar yaml-scalar)

(defn- render-entry!
  "Renders the entry's <harness> target source through Selmer and writes it
  under <out-root>/<to>. Only the OUTPUT location is redirected, so callers can
  generate into a temp dir without touching the working tree (see the `check`
  task)."
  [harness out-root {:keys [slug targets]}]
  (when-let [{:keys [from to]} (get targets harness)]
    (let [target (str (fs/path out-root to))]
      (fs/create-dirs (fs/parent target))
      (spit target (sp/render (slurp (rpath from)) {}))
      (println "  wrote" (name slug) "→" (rel-from-root target)))))

(defn run-harness
  "Renders every targets.yaml entry that targets the given harness into
  `out-root`."
  [harness out-root]
  (let [matched (for [group        (vals config)
                      [slug entry] (:entries group)
                      :when        (get-in entry [:targets harness])]
                  (assoc entry :slug slug))]
    (when (empty? matched)
      (eprintln "No targets found for harness:" (name harness))
      (System/exit 1))
    (run! #(render-entry! harness out-root %) matched)))

(def ^:private cli-spec
  "babashka.cli options: positional <harness> [out-dir] or --harness/--out-dir.
  Missing/invalid harness exits 2; unknown harness exits 1 (see run-harness)."
  {:spec       {:harness {:coerce  :keyword
                          :require true
                          :ref     "<harness>"
                          :desc    "Harness key from targets.yaml (e.g. copilot, eca)."}
                :out-dir {:ref  "<out-dir>"
                          :desc "Output root; defaults to repo root (used by the check task)."}}
   :args->opts [:harness :out-dir]
   :error-fn   (fn [_]
                 (eprintln "Usage: bb scripts/build.clj <harness> [out-dir]")
                 (System/exit 2))})

(defn -main
  "Render all targets for <harness> into out-dir (default repo-root). The
  out-dir override lets the `check` task build into a temp dir."
  [& args]
  (let [{:keys [harness out-dir]} (cli/parse-opts args cli-spec)]
    (run-harness harness (or out-dir repo-root))))

;; Run -main only when run directly, not when loaded at a REPL.
;; https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
