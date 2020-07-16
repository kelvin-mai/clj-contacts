(ns contacts.state
  (:require [helix.core :refer [create-context]]
            [helix.hooks :as hooks]))

(def contacts-state (create-context nil))

(defmulti contact-reducer
  (fn [state action] (first action)))

(defmethod contact-reducer
  :add [state [action payload]]
  (println payload)
  (inc state))

(defn use-contacts-state []
  (let [[state dispatch] (hooks/use-context contacts-state)]
    [state dispatch]))