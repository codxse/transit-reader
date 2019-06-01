(ns transit-reader.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [transit-reader.subs :as subs]
    [cljs.pprint :as pp]
    ))

(defn log [& body]
  (apply js/console.log body))



(defn transit-text-input []
  (let [!text-area (atom nil)]
    (r/create-class
      {:display-name "transit-text-input"
       :reagent-render
                     (fn []
                       [:div {:className "flex flex-column w-50"}
                        [:textarea {:ref   (fn [element] (reset! !text-area element))
                                    :className "tl pa1 mb2 courier"
                                    :type  "text"
                                    :cols  "100"
                                    :rows  "40"}]
                        [:button {:type    "button"
                                  :onClick (fn [e]
                                             (when-let [text-area @!text-area]
                                               (rf/dispatch [:save-input-value (.-value text-area)])))}
                         [:h1 {:style {:padding "0 200px"}} "EDNize"]]])})))

(defn reander-pre [text-value]
  [:div {:className "overflow-auto"
         ;:style {:height "500px"}
         }
   [:textarea {:cols "100"
               :rows "44"
               :className "tl db bg-near-white pa2 courier"
               :value text-value}]])

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])
        raw-input-value (rf/subscribe [:raw-input-value])
        edn-input-value (rf/subscribe [:edn-input-value])]
    [:div {:className "w-100"}
     [:h1 "Transit->EDN"]
     [:div {:className "flex flex-row"}
      [transit-text-input]
      [reander-pre @edn-input-value]]]))