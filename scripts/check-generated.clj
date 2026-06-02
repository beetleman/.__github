#!/usr/bin/env bb
;; Verify committed generated files match a fresh build.
;; Usage: bb scripts/check-generated.clj  (or: mise run check-generated)
;;
;; Builds into a temp dir and diffs against the committed trees (github,
;; eca-plugins). Non-zero exit means drift: run `mise run build` and commit.
;; Only generated files are checked, not static ones like
;; .eca-plugin/marketplace.json.

(require '[babashka.fs :as fs]
         '[babashka.process :as p]
         '[clojure.string :as str]
         '[common :refer [rpath eprintln]])

(defn -main [& _]
  (let [tmp (str (fs/create-temp-dir {:prefix "harness-check"}))]
    (try
      ;; build.clj accepts an optional out-dir as its second arg.
      (p/shell "bb" (rpath "scripts" "build.clj") "copilot" tmp)
      (p/shell "bb" (rpath "scripts" "build.clj") "eca" tmp)

      ;; {:continue true} lets all diffs run before we decide the result.
      (let [statuses
            (for [path ["github" "eca-plugins"]]
              {:ok? (zero? (:exit (p/shell {:continue true}
                                           "difft" "--exit-code" "--skip-unchanged"
                                           "--sort-paths"
                                           (rpath path)
                                           (str (fs/path tmp path)))))
               :path path})
            broken (filter (complement :ok?) statuses)]
        (if (seq broken)
          (do
            (eprintln "ERROR: Committed generated files differ from a fresh build.")
            (eprintln (str "CHANGED: " (str/join ", " (map :path broken))))
            (eprintln "       Run 'mise run build' and commit the results, or fix them if the changes are not intentional.")
            (System/exit 1))
          (println "Check OK: committed generated files match a fresh build.")))
      (finally
        (fs/delete-tree tmp)))))

;; Run -main only when run directly, not when loaded at a REPL.
;; https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
