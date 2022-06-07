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

;; Making the following domain
;; Man vs monster
;;    People
;;      Men
;;        Name
;;        Have Beard? 
;;    Monster
;;      Chupacabra
;;        Eat Goats?

#_(domain man-vs-monster
        (grouping people
                  (Human "A stock human")
                  (Man (isa Human)
                       "A man, dude"
                       [name]
                       [has-beard]))
        (grouping monster
                  (Chupacabra
                    "A fierce, yet elusive"
                    [eats-goats?])))

;; The above is what we want to generate using a macro


;; below code is somewhat complex macro. You should probably study it further

(defmacro domain [name & body]
  `{:tag :domain
    :attrs {:name (str '~name)}
    :content [~@body]})


(declare handle-things)
(defmacro grouping [name & body]
  `{:tag :grouping
    :attrs {:name (str '~name)}
    :content [~@(handle-things body)]})

(declare grok-attrs grok-props)
(defn handle-things [things]
  (for [t things]
    {:tag :thing
     :attrs (grok-attrs (take-while (comp not vector?) t))
     :content (if-let [c (grok-props (drop-while (comp not vector?) t))]
                [c]
                [])}))

(defn grok-attrs [attrs]
  (into {:name (str (first attrs))}
        (for [a (rest attrs)]
          (cond
            (list? a) [:isa (str (second a))]
            (string? a) [:comment a]))))

(defn grok-props [props]
  (when props
    {:tag :properties :attrs nil
     :content (apply vector (for [p props]
                              {:tag :property
                               :attrs {:name (str (first p))}
                               :content nil}))}))



(def dominio (domain man-vs-monster
        (grouping people
                  (Human "A stock human")
                  (Man (isa Human)
                       "A man, dude"
                       [name]
                       [has-beard]))
        (grouping monster
                  (Chupacabra
                    "A fierce, yet elusive"
                    [eats-goats?]))))

(use '[clojure.xml :as xml])

(xml/emit dominio)
