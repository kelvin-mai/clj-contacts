(ns contacts.components.contact-form
  (:require [ajax.core :refer [PUT POST]]
            [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [contacts.state :refer [use-app-state]]
            [contacts.utils :refer [make-label-str
                                    contact-form-fields
                                    api-host]]))

(defnc contact-display-item [{:keys [label value]}]
  (d/p
   (d/strong
    (make-label-str label))
   value))

(defnc contact-display [{:keys [contact]}]
  (<>
   (map-indexed
    (fn [i v]
      ($ contact-display-item {:label v
                               :value (get contact (keyword v))
                               :key i}))
    contact-form-fields)))

(defnc contact-edit-item [{:keys [label value on-change]}]
  (d/div
   (d/label {:for label
             :class '[font-bold]}
            (make-label-str label))
   (d/input {:id label
             :class '[shadow border rounded w-full py-2 px-3 mb-3]
             :value value
             :on-change on-change})))

(defnc contact-edit [{:keys [contact]}]
  (let [[state set-state] (hooks/use-state contact)
        [app-state actions] (use-app-state)
        selected (:selected app-state)
        {:keys [add-contact update-contact]} actions]
    (d/form {:on-submit (fn [e]
                          (.preventDefault e)
                          (if selected
                            (PUT (str api-host "/contacts/" (:id selected))
                              (let [{:keys [first_name last_name email]} state]
                                {:params {:first-name first_name
                                          :last-name last_name
                                          :email email}
                                 :format :json
                                 :handler (fn [response]
                                            (update-contact (:contact response)))}))
                            (POST (str api-host "/contacts")
                              (let [{:keys [first_name last_name email]} state]
                                {:params {:first-name first_name
                                          :last-name last_name
                                          :email email}
                                 :format :json
                                 :handler (fn [response]
                                            (add-contact response))}))))}
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
             contact-form-fields)
            (d/button {:type "submit"
                       :class '[bg-teal-500 py-2 px-4 w-full text-white]}
                      "Submit"))))

(defnc contact-form []
  (let [[edit set-edit] (hooks/use-state false)
        [state actions] (use-app-state)
        selected (:selected state)
        new-contact (:new-contact actions)]
    (hooks/use-effect
     [selected]
     (if (not selected)
       (set-edit true)
       (set-edit false)))
    (d/div
     (d/div {:class '[mb-2 flex justify-between]}
            (d/button {:class '[bg-teal-500 py-1 px-4 rounded text-white]
                       :on-click #(new-contact)}
                      "New contact")
            (when selected
              (d/button {:class '[bg-teal-500 py-1 px-4 rounded text-white]
                         :on-click #(set-edit (not edit))}
                        (if edit
                          "Cancel"
                          "Edit Contact"))))
     (if edit
       ($ contact-edit {:contact selected})
       ($ contact-display {:contact selected})))))
