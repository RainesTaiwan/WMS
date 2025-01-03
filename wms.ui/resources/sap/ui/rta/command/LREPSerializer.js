/*!
 * OpenUI5
 * (c) Copyright 2009-2019 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(["sap/ui/base/ManagedObject","sap/ui/rta/command/FlexCommand","sap/ui/rta/command/AppDescriptorCommand","sap/ui/fl/Utils","sap/ui/fl/Change","sap/ui/fl/registry/Settings","sap/ui/dt/ElementUtil","sap/base/Log","sap/ui/fl/write/api/PersistenceWriteAPI"],function(M,F,A,a,C,S,E,L,P){"use strict";var b=M.extend("sap.ui.rta.command.LREPSerializer",{metadata:{library:"sap.ui.rta",associations:{rootControl:{type:"sap.ui.core.Control"}},properties:{commandStack:{type:"object"}},aggregations:{}}});function g(r){return E.getElementInstance(r);}b.prototype._lastPromise=Promise.resolve();b.prototype.setCommandStack=function(c){if(this.getCommandStack()){this.getCommandStack().removeCommandExecutionHandler(this._fnHandleCommandExecuted);}this.setProperty("commandStack",c);c.addCommandExecutionHandler(this._fnHandleCommandExecuted);};b.prototype.init=function(){this._fnHandleCommandExecuted=this.handleCommandExecuted.bind(this);};b.prototype.exit=function(){this.getCommandStack().removeCommandExecutionHandler(this._fnHandleCommandExecuted);};b.prototype._isPersistedChange=function(p){return!!this.getCommandStack()._aPersistedChanges&&this.getCommandStack()._aPersistedChanges.indexOf(p.getId())!==-1;};b.prototype.handleCommandExecuted=function(e){return(function(e){var p=e;this._lastPromise=this._lastPromise.catch(function(){}).then(function(){var c=this.getCommandStack().getSubCommands(p.command);if(p.undo){c.forEach(function(o){if(!(o instanceof F||o instanceof A)||o.getRuntimeOnly()){return;}var f=o.getPreparedChange();var h=o.getAppComponent();if(h){P.remove({change:f,selector:h});}});}else{var d=[];c.forEach(function(o){if(o.getRuntimeOnly()){return;}if(o instanceof F){var f=o.getAppComponent();if(f){var h=o.getPreparedChange();if(h.getState()===C.states.DELETED){h.setState(C.states.NEW);}if(!this._isPersistedChange(h)){P.add({change:o.getPreparedChange(),selector:f});}}}else if(o instanceof A){d.push(o.createAndStoreChange());}}.bind(this));return Promise.all(d);}}.bind(this));return this._lastPromise;}.bind(this))(e);};b.prototype.needsReload=function(){this._lastPromise=this._lastPromise.catch(function(){}).then(function(){var c=this.getCommandStack().getAllExecutedCommands();return c.some(function(o){return!!o.needsReload;});}.bind(this));return this._lastPromise;};b.prototype.saveCommands=function(){this._lastPromise=this._lastPromise.catch(function(){}).then(function(){var r=g(this.getRootControl());if(!r){throw new Error("Can't save commands without root control instance!");}return P.save({selector:r,skipUpdateCache:false});}.bind(this)).then(function(){L.info("UI adaptation successfully transfered changes to layered repository");this.getCommandStack().removeAllCommands();}.bind(this));return this._lastPromise;};b.prototype._triggerUndoChanges=function(){var c=this.getCommandStack();var p=[];var d=c.getAllExecutedCommands();d.forEach(function(o){p.push(o.undo.bind(o));});p=p.reverse();return a.execPromiseQueueSequentially(p,false,true);};b.prototype.clearCommandStack=function(){var c=this.getCommandStack();c.detachCommandExecuted(this.handleCommandExecuted.bind(this));return this._triggerUndoChanges().then(function(){c.removeAllCommands();c.attachCommandExecuted(this.handleCommandExecuted.bind(this));return true;}.bind(this));};return b;},true);
