(ns contacts.core
  (:require [ajax.core :refer [GET]]
            [hx.react :as hx :refer [defnc]]
            [hx.hooks :as hooks]
            ["react-dom" :as dom]))

(defnc greeting
  [{:keys [name]}]
  [:div "Hello, "
   [:strong name] "!"])

(defnc app []
  (let [[state set-state] (hooks/useState {:name "Helix User"})]
    (prn state)
    [:div
     [:h1 "Welcome!"]
     [greeting {:name (:name state)}]
     [:input {:class '[border shadow]
              :value (:name state)
              :on-change #(set-state assoc :name (.. % -target -value))}]]))

(defn ^:export ^:after-load init []
  (dom/render (hx/f [app]) (js/document.getElementById "app")))

(comment
  (GET "http://localhost:4000/api/contacts"
    {:handler (fn [response]
                (.log js/console response))})
  (init)
  ())