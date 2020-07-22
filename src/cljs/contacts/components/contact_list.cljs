(ns contacts.components.contact-list
  (:require [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [contacts.state :refer [use-app-state]]
            [contacts.components.icons :refer [user-icon]]))

(defnc contact-list-item [{:keys [contact]}]
  (let [[_ actions] (use-app-state)
        set-selected (:select actions)]
    (d/li {:class '[mb-2]}
          (d/div {:class '[flex justify-between]}
                 (d/span
                  ($ user-icon)
                  (d/span {:class '[pl-2]}
                          (str (:first_name contact) " " (:last_name contact))))
                 (d/button {:class '[bg-teal-500 py-1 px-4 rounded text-white focus:bg-teal-300]
                            :on-click #(set-selected contact)}
                           "Select")))))

(defnc contact-list []
  (let [[state _] (use-app-state)
        contacts (:contacts state)]
    (<>
     (d/ul
      (map-indexed (fn [i contact]
                     ($ contact-list-item {:contact contact
                                           :key i}))
                   contacts)))))