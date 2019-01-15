package com.girnarsoft.tracking.event;

import com.girnarsoft.tracking.AnalyticConstant;

import java.util.HashMap;
import java.util.Map;

public class EventInfo {
    private String eventName;
    private Map<String, String> params = new HashMap<>();
    private UserAttributes userAttributes;

    private EventInfo(String eventName, Map<String, String> params, UserAttributes userAttributes) {
        this.eventName = eventName;
        this.params = params;
        this.userAttributes = userAttributes;
    }

    public String getEventName() {
        return eventName;
    }

    public Map<String, String> getEventInfo() {
        return params;
    }

    public UserAttributes getUserAttributes() {
        return userAttributes;
    }

    public enum EventName {
        OPEN_SCREEN("openScreen"), EVENT_TRACKING("eventTracking"),
        BUTTON_CLICK("ButtonClick"), MENU_CLICK("Menu Items"), SHARE("Share"),
        LOGIN("Login");


        private String value;

        EventName(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum EventAction {
        CLICKED("clicked"), REDIRECTED("redirected"), OPEN("open"), SWIPED("swiped"), FILLED("filled"), LIST("listing"), SUBMIT("submit"), ADD("add"), COMPARED("compared"),
        SHARED("shared"), SEARCH_ATTEMPT("Attempt"), NETWORK_CALL("network_call"), CALLNOW("callnow"), GETACALL("getacall"), USER_CALL("user_call"), EDIT_DETAIL("edit_detail"), PREMIUM_CALCULATION("premium_calculation"), SEND_PAYZAPP("send_payzapp"), SEND_QUOTATION("send_quotation"), DOC_UPLOAD("doc_upload"), SEND_APPROVAL("send_approval"), DEP_EDI_DETAIL("dep_edit_detail"), LEAD_STATUS_UPDATE("lead_status_update"), VEHICLE_NUMBER_UPDATE("vehicle_number_update"), DOC_DELETE("doc_delete"), SEND_APPROVED_REJECT("approve_reject"), TPPSM_CALL("tppsm_call"), BDR_CALL("bdr_call"), TASK_ASSIGNE("task_assigne"), TASK_REASSIGNE("task_reassigne");

        private String value;

        EventAction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum EventCategory {
        HAMBURGER_MENU("hamburger_menu"), WIDGETS("widgets"), BUTTONS("buttons"),
        TABS("tabs"), ICONS("icons"),LISTING("listing"),SUB_LISTING("subListing"),DETAIL("detail");

        private String value;

        EventCategory(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class Builder {
        private EventName eventName;
        private Map<String, String> params = new HashMap<>();
        private UserAttributes userAttributes;

        public Builder withUserAttributes(UserAttributes userAttributes) {
            this.userAttributes = userAttributes;
            return this;
        }

        public Builder withEventName(EventName eventName) {
            this.eventName = eventName;
            return this;
        }

        public Builder withPlatform(String platform) {
            params.put("platform", platform);
            return this;
        }

        public Builder withEcode(String ecode) {
            params.put("EmpId", ecode);
            return this;
        }


        public Builder withEventCategory(EventCategory eventCategory) {
            params.put("eventCategory", eventCategory.getValue());
            return this;
        }

        public Builder withEventCategory(String eventCategory) {
            params.put("eventCategory", eventCategory);
            return this;
        }

        public Builder withEventAction(EventAction eventAction) {
            params.put(AnalyticConstant.KEY_ACTION, eventAction.getValue());
            return this;
        }

        public Builder withEventLabel(String eventLabel) {
            params.put("eventLabel", eventLabel);
            return this;
        }

        public Builder withPageType(String pageType) {
            params.put(AnalyticConstant.KEY_PAGE_TYPE, pageType);
            return this;
        }

        public Builder withFailureReason(String failureReason) {
            params.put("failureReason", failureReason);
            return this;
        }


        public Builder withEvent(String event) {
            params.put("event", event);
            return this;
        }

        public Builder withOemName(String oemName) {
            params.put("oemName", oemName);
            return this;
        }

        public Builder withUsedCarId(String usedCarId) {
            params.put("usedCarId", usedCarId);
            return this;
        }

        public Builder withIsSkipLead(String isSkipLead) {
            params.put("isSkipLead", isSkipLead);
            return this;
        }

        public Builder withLastLogEvent(String lastLogEvent) {
            params.put("lastLogEvent", lastLogEvent);
            return this;
        }

        public Builder withUsedActionName(String usedactionname) {
            params.put("usedactionname", usedactionname);
            return this;
        }

        public Builder withUsedActionValue(String usedactionvalue) {
            params.put("usedactionvalue", usedactionvalue);
            return this;
        }

        public Builder withModelName(String modelName) {
            params.put("modelName", modelName);
            return this;
        }

        public Builder withVariantName(String variantName) {
            params.put("variantName", variantName);
            return this;
        }

        public Builder withCarSegment(String carSegment) {
            params.put("carSegment", carSegment);
            return this;
        }

        public Builder withCarPrice(String carPrice) {
            params.put("carPrice", carPrice);
            return this;
        }

        public Builder withEngineCc(String engineCc) {
            params.put("engineCc", engineCc);
            return this;
        }

        public Builder withFuelType(String fuelType) {
            params.put("fuelType", fuelType);
            return this;
        }

        public Builder withTransmissionType(String transmissionType) {
            params.put("transmissionType", transmissionType);
            return this;
        }

        public Builder withOwnerType(String ownerType) {
            params.put("ownerType", ownerType);
            return this;
        }

        public Builder withModelYear(String modelYear) {
            params.put("modelYear", modelYear);
            return this;
        }

        public Builder withKmDriven(String kmDriven) {
            params.put("kmDriven", kmDriven);
            return this;
        }

        public Builder withSellerType(String sellerType) {
            params.put("sellerType", sellerType);
            return this;
        }

        public Builder withCompareCarDetails(String compareCarDetails) {
            params.put("compareCarDetails", compareCarDetails);
            return this;
        }

        public Builder withPriceRange(String priceRange) {
            params.put("priceRange", priceRange);
            return this;
        }

        public Builder withVehicleType(String vehicleType) {
            params.put("vehicleType", vehicleType);
            return this;
        }

        public Builder withCountryCode(String countryCode) {
            params.put("countryId", countryCode);
            return this;
        }

        public Builder withLanguageCode(String languageCode) {
            params.put("language", languageCode);
            return this;
        }

        public Builder withDeeplinkUrl(String deeplinkUrl) {
            params.put("deeplinkUrl", deeplinkUrl);
            return this;
        }


        public Builder withListingType(String listingType) {
            params.put("listingType", listingType);
            return this;
        }

        public Builder withColor(String color) {
            params.put("color", color);
            return this;
        }

        public Builder withTrustmarkCertified(String trustmarkCertified) {
            params.put("trustmarkCertified", trustmarkCertified);
            return this;
        }

        public Builder withDealerName(String dealerName) {
            params.put("dealerName", dealerName);
            return this;
        }

        public Builder withSearchTerm(String term) {
            params.put("SearchTerm", term);
            return this;
        }

        public Builder withUserLocation(String city) {
            params.put("Location", city);
            return this;
        }

        public Builder withStatusData(Object statusData) {
            params.put("Status", statusData.toString());
            return this;
        }

        public Builder withParamMap(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder withLabelMap(String labelName, String value) {
            this.params.put(labelName, value);
            return this;
        }

        public EventInfo build() {
            return new EventInfo(eventName.getValue(), params, userAttributes);
        }


    }
}
