/*!
 * OpenUI5
 * (c) Copyright 2009-2019 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */

sap.ui.define(function () {
	"use strict";

	/**
	 * Provides a history of events. This is needed to show/hide the personalization icon
	 *
	 * @namespace
	 * @alias sap.ui.fl.EventHistory
	 * @experimental Since 1.47.0
	 * @author SAP SE
	 * @version 1.71.0
	 */
	var EventHistory = function () {
	};

	EventHistory._aEventIds = [
		"ControlForPersonalizationRendered"
	];

	EventHistory._aUnsubscribedEventIds = [];

	EventHistory._oHistory = {};

	/**
	 * Starts listening to the events
	 *
	 * @public
	 */
	EventHistory.start = function () {
		EventHistory._aEventIds.forEach(function(sEventId) {
			if (EventHistory._aUnsubscribedEventIds.indexOf(sEventId) === -1) {
				sap.ui.getCore().getEventBus().subscribe("sap.ui", sEventId, EventHistory.saveEvent);
				EventHistory._oHistory[sEventId] = [];
			}
		});
	};

	/**
	 * Saves an event when fired
	 *
	 * @param {string} sChannelId The channel of the event
	 * @param {string} sEventId The identifier of the event
	 * @param {map} mParameters The parameter map carried by the event
	 *
	 * @public
	 */
	EventHistory.saveEvent = function (sChannelId, sEventId, mParameters) {
		var oEvent = {
			channelId: sChannelId,
			eventId: sEventId,
			parameters: mParameters.getId() //we only need the id. In unified.shell sap.ui.getCore().byId(vControl); will be used.
		};

		if (EventHistory._oHistory[sEventId]) {
			var bExists = EventHistory._oHistory[sEventId].some(function(oObject) {
				return (oObject.channelId === oEvent.channelId &&
					oObject.eventId === oEvent.eventId &&
					oObject.parameters === oEvent.parameters);
			});
			if (!bExists) {
				EventHistory._oHistory[sEventId].push(oEvent);
			}
		}
	};

	/**
	 * Returns the history of the event and stops listening
	 *
	 * @param {string} sEventId The identifier of the event
	 *
	 * @return {array} List of events
	 *
	 * @public
	 */
	EventHistory.getHistoryAndStop = function (sEventId) {
		sap.ui.getCore().getEventBus().unsubscribe("sap.ui", sEventId, EventHistory.saveEvent);
		EventHistory._addUnsubscribedEvent(sEventId);
		return EventHistory._oHistory[sEventId] || [];
	};

	EventHistory._addUnsubscribedEvent = function(sEventId) {
		if (EventHistory._aUnsubscribedEventIds.indexOf(sEventId) === -1) {
			EventHistory._aUnsubscribedEventIds.push(sEventId);
		}
	};

	return EventHistory;
}, /* bExport= */true);
