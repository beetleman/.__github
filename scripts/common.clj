(ns common
  "Shared helpers for the build/task scripts. Owns two things that must stay
   consistent across build.clj and the task scripts: the repo-root resolution
   and the parsed targets.yaml (the single source of truth for what gets
   generated and where)."
  (:require [babashka.fs :as fs]
            [clj-yaml.core :as yaml]))

(def repo-root
  "Absolute path to the repository root (the parent of scripts/).
   `*file*` is set when bb loads this namespace; the fallback covers REPL/eval
   contexts where `*file*` is nil and we assume bb was invoked from repo root."
  (-> (or *file* (str (fs/cwd) "/scripts/common.clj"))
      fs/absolutize
      fs/parent
      fs/parent
      str))

(defn rpath
  "Builds an absolute path under repo-root from path segments."
  [& parts]
  (str (apply fs/path repo-root parts)))

(defn eprintln
  "Print to stderr."
  [& args]
  (binding [*out* *err*] (apply println args)))

(def config
  "Parsed targets.yaml. Every top-level group has the same shape:
     <group> -> :entries -> <slug> -> :targets -> <harness> -> {:from :to}"
  (-> (rpath "scripts" "targets.yaml") slurp (yaml/parse-string :keywords true)))

(defn target-paths
  "Every generated output FILE path (the `to` of each entry/harness target),
   relative to repo-root. Walks the uniform group/entry/target shape so it
   needs no hardcoded group names."
  []
  (distinct
   (for [[_ group]  config
         [_ entry]  (:entries group)
         [_ target] (:targets entry)]
     (:to target))))
