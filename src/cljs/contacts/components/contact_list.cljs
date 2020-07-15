(ns contacts.components.contact-list
  (:require [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]))

(defnc contact-list-item [{:keys [contact]}]
  (d/li
   (d/div
    (d/p (str (:first_name contact) " " (:last_name contact))))))

(defnc contact-list [{:keys [contacts]}]
  (<>
   (d/h3 "Contacts")
   (d/ul
    (map-indexed (fn [i contact]
                   ($ contact-list-item {:contact contact
                                         :key i}))
                 contacts))))
