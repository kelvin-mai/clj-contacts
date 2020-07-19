(ns contacts.components.contact-list
  (:require [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [contacts.state :refer [use-app-state]]))

(defnc contact-list-item [{:keys [contact]}]
  (d/li
   (d/div
    (d/p (str (:first_name contact) " " (:last_name contact))))))

(defnc contact-list []
  (let [state (use-app-state)
        [contacts _] (:contacts state)]
    (<>
     (d/h3 "Contacts")
     (d/ul
      (map-indexed (fn [i contact]
                     ($ contact-list-item {:contact contact
                                           :key i}))
                   contacts)))))