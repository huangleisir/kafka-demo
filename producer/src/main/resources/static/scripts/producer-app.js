var producerModule = angular.module('Producer', ['ui.bootstrap']);

producerModule.factory('ProducerHelper', function() {
    var buildIndex = function (source, property) {
        console.log('here');
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

producerModule.service('ProducerModel', function() {
    var service = this,
        uplinkMessages = [
            //{body: 'first uplink message'}
        ],
        downlinkMessages = [
            //{body: 'first downlink message'}
        ];

    service.getUplinkMessages = function() {
        return uplinkMessages;
    };

    service.getDownlinkMessages = function() {
        return downlinkMessages;
    };
});

producerModule.controller('MainCtrl', function(ProducerModel, ProducerHelper) {
    var main = this;

    main.uplinkMessages = ProducerModel.getUplinkMessages();
    main.downlinkMessages = ProducerModel.getDownlinkMessages();

    main.uplinkMessagesIndex = ProducerHelper.buildIndex(main.uplinkMessages, 'body');
    main.downlinkMessagesIndex = ProducerHelper.buildIndex(main.downlinkMessages, 'body');

    main.setUplinkMessage = function(uplinkMessage) {
        main.uplinkMessage = uplinkMessage;
    };

    main.sendMessage = function() {
        main.uplinkMessages.push({
            body: main.uplinkMessage
        });
        main.uplinkMessage = "";
    }
});
