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
        topic01Messages = [
            //{body: 'first topic-01 message'}
        ],
        topic02Messages = [
            //{body: 'first topic-02 message'}
        ],
        topic03Messages = [
            //{body: 'first topic-03 message'}
        ],
        topic04Messages = [
            //{body: 'first topic-04 message'}
        ];

    service.getTopic01Messages = function() {
        return topic01Messages;
    };
    service.getTopic02Messages = function() {
        return topic02Messages;
    };
    service.getTopic03Messages = function() {
        return topic03Messages;
    };
    service.getTopic04Messages = function() {
        return topic04Messages;
    };
});

consumerModule.service('ListenService', function($q, $timeout) {
    var service = this, listener = $q.defer(), socket = {
        client: null,
        stomp: null
    }, messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/spring-ng-chat/chat";
    service.TOPIC_01 = "/topic/message/topic-01";
    service.TOPIC_02 = "/topic/message/topic-02";
    service.TOPIC_03 = "/topic/message/topic-03";
    service.TOPIC_04 = "/topic/message/topic-04";
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

    var startTopic01Listener = function() {
        socket.stomp.subscribe(service.TOPIC_01, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startTopic02Listener = function() {
        socket.stomp.subscribe(service.TOPIC_02, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startTopic03Listener = function() {
        socket.stomp.subscribe(service.TOPIC_03, function(data) {
            listener.notify(getMessage(data.body));
        });
    };
    var startTopic04Listener = function() {
        socket.stomp.subscribe(service.TOPIC_04, function(data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function() {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startTopic01Listener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});

consumerModule.controller('MainCtrl', function(ConsumerModel, ConsumerHelper, $http) {
    var main = this;

    main.topic01Messages = ConsumerModel.getTopic01Messages();
    main.topic02Messages = ConsumerModel.getTopic02Messages();
    main.topic03Messages = ConsumerModel.getTopic03Messages();
    main.topic04Messages = ConsumerModel.getTopic04Messages();

    main.topic01MessagesIndex = ConsumerModel.buildIndex(main.topic01Messages, 'body');
    main.topic02MessagesIndex = ConsumerModel.buildIndex(main.topic02Messages, 'body');
    main.topic03MessagesIndex = ConsumerModel.buildIndex(main.topic03Messages, 'body');
    main.topic04MessagesIndex = ConsumerModel.buildIndex(main.topic04Messages, 'body');

    main.setTopic01Message = function(topic01Message) {
        main.topic01Message = topic01Message;
    };
    main.setTopic02Message = function(topic02Message) {
        main.topic02Message = topic02Message;
    };
    main.setTopic03Message = function(topic03Message) {
        main.topic03Message = topic03Message;
    };
    main.setTopic04Message = function(topic04Message) {
        main.topic04Message = topic04Message;
    };

    main.sendTopic01Message = function() {
        console.log('sendTopic01Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-01', message:main.topic01Message});
        main.topic01Messages.push({
            body: main.topic01Message
        });
    };
    main.sendT2Message = function() {
        console.log('sendTopic02Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-02', message:main.topic02Message});
        main.topic02Messages.push({
            body: main.topic02Message
        });
    };
    main.sendTopic03Message = function() {
        console.log('sendTopic03Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-03', message:main.topic03Message});
        main.topic03Messages.push({
            body: main.topic03Message
        });
    };
    main.sendT4Message = function() {
        console.log('sendT4Message');
        $http.post('/rest/api/v1/messages', {topic:'topic-04', message:main.topic04Message});
        main.topic04Messages.push({
            body: main.topic04Message
        });
    };
});
