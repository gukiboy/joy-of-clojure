(ns core)


(defn xors [max-x max-y]
  (for [x (range max-x)
        y (range max-y)]
    [x y (bit-xor x y)]))


(def frame (java.awt.Frame.))

(->> java.awt.Frame .getMethods (map #(.getName %)) (filter #(re-find #"Vis" %)))

(for [methodz (.getMethods java.awt.Frame)
      :let [name (.getName methodz)]
      :when (re-find #"Vis" name)]
  name)

(def gfx (.getGraphics frame))
;; REally cool
(do 
  (.setVisible frame true)
  (.setSize frame (java.awt.Dimension. 800 800))
  (let [gfx (.getGraphics frame)]
    (doseq [[x y xor] (xors 800 800)]
      (.setColor gfx (java.awt.Color. xor xor xor))
      (.fillRect gfx x y 1 1))))

(clojure.stacktrace/e)
