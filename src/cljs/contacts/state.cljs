(ns contacts.state
  (:require [helix.core :refer [create-context]]
            [helix.hooks :as hooks]))

(def initial-state {:selected 0
                    :contacts []})

(def app-state (create-context nil))

(defmulti app-reducer
  (fn [_ action] (first action)))

(defmethod app-reducer
  :set-selected [state [_ payload]]
  (assoc state :selected payload))

(defmethod app-reducer
  :set-contacts [state [_ payload]]
  (assoc state :contacts payload))

(defmethod app-reducer
  :add-contact [state [_ payload]]
  (assoc state :contacts
         (conj state payload)))

(defmethod app-reducer
  :update-contact [state [_ payload]]
  (let [[{:keys [id v]}] payload]
    (assoc state :contacts
           (map (fn [item]
                  (if (= (:id item) id)
                    v
                    item)
                  state)))))

(defmethod app-reducer
  :remove-contact [state [_ payload]]
  (assoc state :contacts
         (filter #(= (:id %) payload) state)))

(defn use-app-state []
  (let [[state dispatch] (hooks/use-context app-state)]
    [state {:init (fn [response]
                    (dispatch [:set-selected (first response)])
                    (dispatch [:set-contacts response]))}]))