(ns transit-reader.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    [transit-reader.subs :as subs]
    [cljs.pprint :as pp]
    [re-view.re-frame-simple :as db]
    [goog.object :as gobj]
    ))

(defn log [& body]
  (apply js/console.log body))

(defn get-title [convert-mode]
  (case convert-mode
    "transit" "Transit->EDN"
    "edn" "EDN->Transit"
    "????"))

(defn output-value [mode]
  (let [edn-input-value (rf/subscribe [:edn-input-value])
        transit-input-value (rf/subscribe [:transit-input-value])]
    (case mode
      "transit" @edn-input-value
      "edn" @transit-input-value
      "Bad input")))

(defn transit-text-input []
  (let [!text-area (atom nil)]
    (r/create-class
      {:display-name "transit-text-input"
       :reagent-render
                     (fn []
                       [:div {:className "flex flex-column w-50"}
                        [:textarea {:ref       (fn [element] (reset! !text-area element))
                                    :className "tl pa1 mb2 courier"
                                    :type      "text"
                                    :value     (db/get :raw-input-value)
                                    :onChange  (fn [e] (db/assoc! :raw-input-value (gobj/getValueByKeys e #js ["target" "value"])))
                                    :cols      "100"
                                    :rows      "40"}]
                        [:div {}
                         [:input {:type     "radio"
                                  :value    "transit"
                                  :checked  (= "transit" (db/get-in [::mode] "transit"))
                                  :onChange #(db/assoc-in! [::mode] (-> % .-target .-value))}]
                         [:label "Transit"]
                         [:input {:type     "radio"
                                  :value    "edn"
                                  :checked  (= "edn" (db/get-in [::mode]))
                                  :onChange #(db/assoc-in! [::mode] (-> % .-target .-value))}]
                         [:label "EDN"]]
                        [:button {:type    "button"
                                  :onClick (fn [e]
                                             (when-let [text-area @!text-area]
                                               (rf/dispatch [:save-input-value (.-value text-area)])))}
                         [:h1 {:style {:padding "0 200px"}} (get-title (db/get-in [::mode]))]]])})))

(defn reander-pre [text-value]
  [:div {:className "overflow-auto"
         ;:style {:height "500px"}
         }
   [:textarea {:cols      "100"
               :rows      "44"
               :onChange  #()
               :className "tl db bg-near-white pa2 courier"
               :value     text-value}]])

(defn main-panel []
  (let [name (rf/subscribe [::subs/name])
        raw-input-value (rf/subscribe [:raw-input-value])
        edn-input-value (rf/subscribe [:edn-input-value])
        transit-input-value (rf/subscribe [:transit-input-value])
        convert-mode (db/get-in [::mode])]
    [:div {:className "w-100"}
     [:h1 (get-title convert-mode)]
     [:div {:className "flex flex-row"}
      [transit-text-input]
      [reander-pre (output-value convert-mode)]]]))