(ns core)


(defn xors [max-x max-y]
  (for [x (range max-x)
        y (range max-y)]
    [x y (rem (bit-xor x y) 256)]))

(defn f-values [f xs ys]
  (for [x (range xs)
        y (range ys)]
    [x y (rem (f x y) 256)]))

(defn clear [g] (.clearRect g 0 0 200 200))

(defn draw-values [frame f xs ys]
  (.clearRect (.getGraphics frame) 0 0 xs ys)
  (.setVisible frame true)
  (.setSize frame (java.awt.Dimension. xs ys))
  (let [gfx (.getGraphics frame)]
    (doseq [[x y v] (f-values f xs ys)]
      (.setColor gfx (java.awt.Color. v v v))
      (.fillRect gfx x y 1 1))))



(def frame (java.awt.Frame.))


(comment 
  (.setVisible frame true)

(draw-values frame (comp + +) 800 800)
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

(defn clear [g] (.clearRect g 0 0 200 200))

(clear gfx)

(clojure.stacktrace/e))
