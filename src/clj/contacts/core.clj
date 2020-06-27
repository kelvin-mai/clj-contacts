(ns contacts.core
  (:require [org.httpkit.server :refer [run-server]]
            [reitit.ring :as ring]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [muuntaja.core :as m]
            [clojure.pprint :refer [pprint]]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    [["/" {:get (fn [req]
                  (pprint req)
                  {:status 200
                   :body "it works!"})}]
     ["/api"
      ["/ping" {:name ::ping
                :get (fn [{:keys [body-params]}]
                       (pprint body-params)
                       {:status 200
                        :body {:hello "world"}})}]]]
    {:data {:muuntaja m/instance
            :middleware [format-negotiate-middleware
                         format-response-middleware
                         exception-middleware
                         format-request-middleware]}})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Server started")
  (reset! server (run-server app {:port 4000})))

(defn restart-server []
  (stop-server)
  (-main))

(comment
  (stop-server)
  (restart-server))