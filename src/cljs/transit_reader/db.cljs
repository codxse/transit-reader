(ns transit-reader.db)

(def default-db
  {:name "re-frame",
   :transit-reader.views/mode "edn",
   :raw-input-value
   "{:string-example \"Mirror Mirror on the World\",\n :map-example {:name \"Who Fang\"},\n :vector-example [\"a\" \"b\" \"c\" \"d\" 0.1 0.2 0.3 1 2 3 true false nil],\n :list-example (true false nil)}\n"}
  )
