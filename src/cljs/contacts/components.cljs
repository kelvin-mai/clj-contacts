(ns contacts.components
  (:require [helix.core :refer [defnc $]]
            [helix.dom :as d]))

(defnc nav []
  (d/nav {:class '[py-4 shadow]}
         (d/div {:class '[container]}
                (d/h2 {:class '[text-xl]} "Contact Book"))))

(defnc contact-list-item [{:keys [contact]}]
  (d/li
   (d/div
    (d/p (str (:first_name contact) " " (:last_name contact))))))

(defnc contact-list [{:keys [contacts]}]
  (d/ul
   (map-indexed (fn [i contact]
                  ($ contact-list-item {:contact contact
                                        :key i}))
                contacts)))