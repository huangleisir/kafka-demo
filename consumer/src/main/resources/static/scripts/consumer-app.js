var consumerModule = angular.module('Consumer', ['ui.bootstrap']);

consumerModule.factory('ConsumerHelper', function() {
    var buildIndex = function (source, property) {
        var tempArray = [];

        for (var i = 0, len = source.length; i < len; ++i) {
            tempArray[source[i][property]] = source[i];
        }

        return tempArray;
    };

    return {
        buildIndex: buildIndex
    };
});

consumerModule.service('ConsumerModel', function() {
    var service = this,
        testMessages = [
            //{body: 'first test message'}
        ],
        smMessages = [
            //{body: 'first signalMetadata-ingest message'}
        ],
        gsmMessages = [
            //{body: 'first gatewayStatusMessage-ingest message'}
        ],
        upmMessages = [
            //{body: 'first uploadPayloadMetadata-ingest message'}
        ],
        downlinkMessages = [
            //{body: 'first downlink message'}
        ];

    service.getTestMessages = function() {
        return testMessages;
    };

    service.getSmMessages = function() {
        return smMessages;
    };

    service.getGsmMessages = function() {
        return gsmMessages;
    };

    service.getUpmMessages = function() {
        return upmMessages;
    };

    service.getDownlinkMessages = function() {
        return downlinkMessages;
    };
});

consumerModule.service('ListenService', function($q, $timeout) {
    var service = this, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/spring-ng-chat/chat";
    service.CHAT_TOPIC = "/topic/message";
    service.CHAT_BROKER = "/app/chat";

    service.receive = function() {
        return listener.promise;
    };

    var reconnect = function() {
        $timeout(function() {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function(data) {
        var message = JSON.parse(data), out = {};
        out.message = message.message;
        out.time = new Date(message.time);
        if (_.contains(messageIds, message.id)) {
            out.self = true;
            messageIds = _.remove(messageIds, message.id);
        }
        return out;
    };

    var startListener = function() {
        socket.stomp.subscribe(service.CHAT_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});

consumerModule.controller('MainCtrl', function(ConsumerModel, ConsumerHelper, $http) {
    var main = this;

    main.testMessages = ConsumerModel.getTestMessages();
    main.smMessages = ConsumerModel.getSmMessages();
    main.gsmMessages = ConsumerModel.getGsmMessages();
    main.upmMessages = ConsumerModel.getUpmMessages();
    main.downlinkMessages = ConsumerModel.getDownlinkMessages();

    main.testMessagesIndex = ConsumerModel.buildIndex(main.testMessages, 'body');
    main.smMessagesIndex = ConsumerModel.buildIndex(main.smMessages, 'body');
    main.gsmMessagesIndex = ConsumerModel.buildIndex(main.gsmMessages, 'body');
    main.upmMessagesIndex = ConsumerModel.buildIndex(main.upmMessages, 'body');
    main.downlinkMessagesIndex = ConsumerModel.buildIndex(main.downlinkMessages, 'body');

    main.setTestMessage = function(testMessage) {
        main.testMessage = testMessage;
    };

    main.setSmMessage = function(smMessage) {
        main.smMessage = smMessage;
    };

    main.setGsmMessage = function(gsmMessage) {
        main.gsmMessage = gsmMessage;
    };

    main.setUpmMessage = function(upmMessage) {
        main.upmMessage = upmMessage;
    };

    main.setDownlinkMessage = function(downlinkMessage) {
        main.downlinkMessage = downlinkMessage;
    };

    main.sendMessage = function() {
        console.log('sendMessage');
        $http.post('/rest/api/v1/messages', {topic:'test', message:main.uplinkMessage});
        main.uplinkMessages.push({
            body: main.uplinkMessage
        });
        main.uplinkMessage = "";
    }
});
