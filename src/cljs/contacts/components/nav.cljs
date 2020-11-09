(ns contacts.components.nav
  (:require [helix.core :refer [defnc]]
            [helix.dom :as d]))

(defnc nav []
  (d/nav {:class '[py-4 shadow]}
         (d/div {:class '[container]}
                (d/h2 {:class '[text-xl]} "Contact Book"))))
