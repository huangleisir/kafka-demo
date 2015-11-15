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
        t1Messages = [
            //{body: 'first topic-01 message'}
        ],
        t2Messages = [
            //{body: 'first topic-02 message'}
        ],
        t3Messages = [
            //{body: 'first topic-03 message'}
        ],
        t4Messages = [
            //{body: 'first topic-04 message'}
        ];

    service.getTestMessages = function() {
        return testMessages;
    };

    service.getT1Messages = function() {
        return t1Messages;
    };

    service.getT2Messages = function() {
        return t2Messages;
    };

    service.getT3Messages = function() {
        return t3Messages;
    };

    service.getT4Messages = function() {
        return t4Messages;
    };
});

consumerModule.service('ListenService', function($q, $timeout) {
    var service = this, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/spring-ng-chat/chat";
    service.TEST_TOPIC = "/topic/message/test";
    service.T1_TOPIC = "/topic/message/topic-01";
    service.T2_TOPIC = "/topic/message/topic-02";
    service.T3_TOPIC = "/topic/message/topic-03";
    service.T4_TOPIC = "/topic/message/topic-04";
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

    var startTestListener = function() {
        socket.stomp.subscribe(service.TEST_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startT1Listener = function() {
        socket.stomp.subscribe(service.T1_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startT2Listener = function() {
        socket.stomp.subscribe(service.T2_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startT3Listener = function() {
        socket.stomp.subscribe(service.T3_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startT4Listener = function() {
        socket.stomp.subscribe(service.T4_TOPIC, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startTestListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});

consumerModule.controller('MainCtrl', function(ConsumerModel, ConsumerHelper, $http) {
    var main = this;

    main.testMessages = ConsumerModel.getTestMessages();
    main.t1Messages = ConsumerModel.getT1Messages();
    main.t2Messages = ConsumerModel.getT2Messages();
    main.t3Messages = ConsumerModel.getT3Messages();
    main.t4Messages = ConsumerModel.getT4Messages();

    main.testMessagesIndex = ConsumerModel.buildIndex(main.testMessages, 'body');
    main.t1MessagesIndex = ConsumerModel.buildIndex(main.t1Messages, 'body');
    main.t2MessagesIndex = ConsumerModel.buildIndex(main.t2Messages, 'body');
    main.t3MessagesIndex = ConsumerModel.buildIndex(main.t3Messages, 'body');
    main.t4MessagesIndex = ConsumerModel.buildIndex(main.t4Messages, 'body');

    main.setTestMessage = function(testMessage) {
        main.testMessage = testMessage;
    };
    main.setT1Message = function(t1Message) {
        main.t1Message = t1Message;
    };
    main.setT2Message = function(t2Message) {
        main.t2Message = t2Message;
    };
    main.setT3Message = function(t3Message) {
        main.t3Message = t3Message;
    };
    main.setT4Message = function(t4Message) {
        main.t4Message = t4Message;
    };

    main.sendTestMessage = function() {
        console.log('sendTestMessage');
        $http.post('/rest/api/v1/messages', {topic:'test', message:main.testMessage});
        main.testMessages.push({
            body: main.testMessage
        });
    };
    main.sendT1Message = function() {
        console.log('sendT1Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-01', message:main.t1Message});
        main.uplinkMessages.push({
            body: main.uplinkMessage
        });
    };
    main.sendT2Message = function() {
        console.log('sendT2Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-02', message:main.t2Message});
        main.t2Messages.push({
            body: main.t2Message
        });
    };
    main.sendT3Message = function() {
        console.log('sendT3Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-03', message:main.t3Message});
        main.t3Messages.push({
            body: main.t3Message
        });
    };
    main.sendT4Message = function() {
        console.log('sendT4Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-04', message:main.t4Message});
        main.t4Messages.push({
            body: main.t4Message
        });
    };
});
