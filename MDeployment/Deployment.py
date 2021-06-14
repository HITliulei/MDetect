import requests
import json
import random
import tornado.ioloop
import tornado.web
import time

ip_master = "172.31.43.94"
k8s_url = 'http://'+ip_master+':18082/api/v1/controlk8s'



class Service:
    def __init__(self, serviceVersion, serviceName, port):
        self.serviceVersion = serviceVersion
        self.serviceName = serviceName
        self.port = port

    def __str__(self):
        return self.__str__()


serviceList = [
    Service('v1.0.0', 'ts-voucher-service', 16101),
    Service('v1.0.0', 'ts-verification-code-service', 15678),
    Service('v1.0.0', 'ts-travel-service' , 12346),
    Service('v1.0.0', 'ts-travel-plan-service', 14322),
    Service('v1.0.0', 'ts-travel2-service' , 16346),
    Service('v1.0.0', 'ts-train-service' , 14567),
    Service('v1.0.0', 'ts-ticketinfo-service', 15681),
    Service('v1.0.0', 'ts-ticket-office-service' , 16108),
    Service('v1.0.0', 'ts-station-service', 12345),
    Service('v1.0.0', 'ts-auth-service' , 12340),
    Service('v1.0.0', 'ts-security-service' , 11188),
    Service('v1.0.0', 'ts-seat-service' , 18898),
    Service('v1.0.0', 'ts-route-service' , 11178),
    Service('v1.0.0', 'ts-route-plan-service', 14578),
    Service('v1.0.0', 'ts-rebook-service' , 18886),
    Service('v1.0.0', 'ts-price-service' , 16579),
    Service('v1.0.0', 'ts-preserve-service' , 14568),
    Service('v1.0.0', 'ts-preserve-other-service' , 14569),
    Service('v1.0.0', 'ts-payment-service' , 19001),
    Service('v1.0.0', 'ts-order-service' , 12031),
    Service('v1.0.0', 'ts-order-other-service' , 12032),
    Service('v1.0.0', 'ts-news-service' , 12862),
    Service('v1.0.0', 'ts-notification-service' , 17853),
    Service('v1.0.0', 'ts-user-service' , 12342),
    Service('v1.0.0', 'ts-inside-payment-service' , 18673),
    Service('v1.0.0', 'ts-food-service' , 18856),
    Service('v1.0.0', 'ts-food-map-service' , 18855),
    Service('v1.0.0', 'ts-execute-service' , 12386),
    Service('v1.0.0', 'ts-contacts-service' , 12347),
    Service('v1.0.0', 'ts-consign-service' , 16111),
    Service('v1.0.0', 'ts-consign-price-service', 16110),
    Service('v1.0.0', 'ts-config-service' , 15679),
    Service('v1.0.0', 'ts-cancel-service', 18885),
    Service('v1.0.0', 'ts-basic-service' , 15680),
    Service('v1.0.0', 'ts-assurance-service' , 18888),
    Service('v1.0.0', 'ts-admin-user-service' , 16115),
    Service('v1.0.0', 'ts-admin-travel-service' , 16114),
    Service('v1.0.0', 'ts-admin-route-service' , 16113),
    Service('v1.0.0', 'ts-admin-order-service' , 16112),
    Service('v1.0.0', 'ts-admin-basic-info-service' , 18767)
]




