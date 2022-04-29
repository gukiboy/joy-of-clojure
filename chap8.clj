(ns chap8)

(defn contextual-eval [ctx expr]
  (eval `(let [~@(mapcat (fn [[k v]] [k `'~v]) ctx)]
           ~expr)))

(contextual-eval '{a 1 b 2} '(+ a b))

; extreme syntax quote usage:
(let [x 9
      y '(- x)]
  (println `y)
  (println ``y)
  (println ``~y)
  (println ``~~y)
  (contextual-eval {'x 36} ``~~y)
  )

(macroexpand `(-> {} 
                  (assoc :monke "macaco")
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                  (assoc :frong 20)
                 ))

(defn monke-string [& strings]
  strings)

(monke-string "banana" "maca" "macaco")



(macroexpand-1 `(-> {} 
                  (assoc :monke "macaco")
                  (assoc :frong 20)
                 ))

(macroexpand-1 `when)

(defmacro do-until [& clauses]
  (when clauses
    (list 'clojure.core/when (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (IllegalArgumentException. "do-until requires an even number of forms")))
          (cons 'do-until (nnext clauses)))))

(do-until
  true (println "Banana")
  true (println "MOnke"))

(macroexpand `(if (not condition) "got it"))


;; watches a variable?!??!
(defmacro def-watched [name & value]
  `(do
     (def ~name ~@value)
     (add-watch (var ~name)
                :re-bind
                (fn [~'key ~'r old# new#]
                  (println old# " -> " new#)))))

(def-watched x (* 12 12))

(def x 0)


;kinda tricky
(clojure.walk/macroexpand-all `(-> {} 
                  (assoc :monke "macaco")
                  (assoc :frong 20)
                 ))
