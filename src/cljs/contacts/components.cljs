(ns contacts.components
  (:require [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [contacts.utils :refer [make-label-str
                                    contact-form-fields]]))

(defnc nav []
  (d/nav {:class '[py-4 shadow]}
         (d/div {:class '[container]}
                (d/h2 {:class '[text-xl]} "Contact Book"))))

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

(defnc contact-display-item [{:keys [label value]}]
  (d/p
   (d/strong
    (make-label-str label)
    value)))

(defnc contact-display [{:keys [contact]}]
  (<>
   (map-indexed
    (fn [i v]
      ($ contact-display-item {:label v
                               :value (get  contact (keyword v))
                               :key i}))
    contact-form-fields)))

(defnc contact-edit-item [{:keys [label value on-change]}]
  (d/div
   (d/label {:for label}
            (make-label-str label))
   (d/input {:id label
             :value value
             :on-change on-change})))

(defnc contact-edit [{:keys [contact]}]
  (let [[state set-state] (hooks/use-state contact)]
    (prn state)
    (d/form
     (map-indexed
      (fn [i v]
        ($ contact-edit-item {:label v
                              :value (get state (keyword v))
                              :key i
                              :on-change #(set-state
                                           (assoc state
                                                  (keyword v)
                                                  (.. %
                                                      -target
                                                      -value)))}))
      contact-form-fields))))

(defnc contact [{:keys [contact]}]
  (let [[edit set-edit] (hooks/use-state false)]
    (d/div
     (d/h1 "Contact")
     (d/button {:on-click #(set-edit (not edit))}
               (if edit
                 "Cancel"
                 "Edit contact"))
     (if edit
       ($ contact-display {:contact contact})
       ($ contact-edit {:contact contact})))))
