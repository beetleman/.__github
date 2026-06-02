#!/usr/bin/env bb
;; Remove generated outputs. Does NOT touch src/ or snapshots.
;; Usage: bb scripts/clean.clj  (or: mise run clean)
;;
;; The file set is derived from targets.yaml, so new artifacts need no change here.

(ns clean
  (:require [babashka.fs :as fs]
            [common :refer [repo-root rpath target-paths]]))

(defn- prune-empty-parents!
  "Walk up from `path`'s parent, deleting each directory that is empty, until
   a non-empty dir or the repo root is reached."
  [path]
  (loop [dir (fs/parent path)]
    (when (and dir
               (not= (str dir) repo-root)
               (fs/directory? dir)
               (empty? (fs/list-dir dir)))
      (fs/delete dir)
      (println "  removed empty dir" (str (fs/relativize repo-root dir)))
      (recur (fs/parent dir)))))

(defn -main [& _]
  (doseq [p (target-paths)
          :let [target (rpath p)]]
    (when (fs/exists? target)
      (fs/delete target)
      (println "  removed" p)
      (prune-empty-parents! target)))
  (println "Clean OK."))

;; Run -main only when run directly, not when loaded at a REPL.
;; https://book.babashka.org/#main_file
(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
