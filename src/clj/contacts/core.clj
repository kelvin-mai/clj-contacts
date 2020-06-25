(ns contacts.core
  (:require [org.httpkit.server :refer [run-server]]
            [reitit.ring :as ring]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
   (ring/router
    [["/api"
      ["/ping" {:name ::ping
                :get (fn [_] {:status 200
                              :body "ok"})}]]])))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil)))

(defn -main []
  (println "Server started")
  (reset! server (run-server app {:port 4000})))

(comment
  @server
  (-main)
  (stop-server)
  ())