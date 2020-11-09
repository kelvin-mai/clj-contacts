(ns contacts.core
  (:require [contacts.routes :refer [ping-routes contact-routes]]
            [muuntaja.core :as m]
            [org.httpkit.server :refer [run-server]]
            [reitit.coercion.schema]
            [reitit.ring :as ring]
            [reitit.ring.coercion :refer [coerce-exceptions-middleware
                                          coerce-request-middleware
                                          coerce-response-middleware]]
            [reitit.ring.middleware.exception :refer [exception-middleware]]
            [reitit.ring.middleware.muuntaja :refer [format-negotiate-middleware
                                                     format-request-middleware
                                                     format-response-middleware]]
            [reitit.ring.middleware.parameters :refer [parameters-middleware]]
            [ring.middleware.cors :refer [wrap-cors]]))

(defonce server (atom nil))

(def app
  (ring/ring-handler
    (ring/router
      [["/api"
        ping-routes
        contact-routes]]
      {:data {:coercion reitit.coercion.schema/coercion
              :muuntaja m/instance
              :middleware [[wrap-cors
                            :access-control-allow-origin [#"http://localhost:4200"]
                            :access-control-allow-methods [:get :post :put :delete]]
                           parameters-middleware
                           format-negotiate-middleware
                           format-response-middleware
                           exception-middleware
                           format-request-middleware
                           coerce-exceptions-middleware
                           coerce-request-middleware
                           coerce-response-middleware]}})
    (ring/routes
      (ring/redirect-trailing-slash-handler)
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "Route not found"})}))))

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

