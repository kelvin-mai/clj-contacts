(ns contacts.core
  (:require [ajax.core :refer [GET]]
            [helix.core :refer [defnc $ <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            ["react-dom" :as dom]
            [contacts.components :refer [nav contact-list]]))

(defnc app []
  (let [[contacts set-contacts] (hooks/use-state [])]
    (hooks/use-effect
     :once
     (GET "http://localhost:4000/api/contacts"
       {:handler #(set-contacts %)}))
    (<>
     ($ nav)
     (d/div {:class '[container pt-4]}
            ($ contact-list {:contacts contacts})))))

(defn ^:export ^:after-load init []
  (dom/render ($ app) (js/document.getElementById "app")))

(comment
  (GET "http://localhost:4000/api/contacts"
    {:handler (fn [response]
                (.log js/console response))})
  (init)
  ())