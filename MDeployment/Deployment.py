import requests
import json
import random
import tornado.ioloop
import tornado.web
import time

ip_master = "172.31.43.94"
k8s_url = 'http://'+ip_master+':18082/api/v1/controlk8s'


class Service:
    def __init__(self, serviceName, port):
        self.serviceName = serviceName
        self.port = port

    def __str__(self):
        return self.__str__()


serviceList = [
    Service('ts-voucher-service', 16101),
    Service('ts-verification-code-service', 15678),
    Service('ts-travel-service' , 12346),
    Service('ts-travel-plan-service', 14322),
    Service('ts-travel2-service' , 16346),
    Service('ts-train-service' , 14567),
    Service('ts-ticketinfo-service', 15681),
    Service('ts-ticket-office-service' , 16108),
    Service('ts-station-service', 12345),
    Service('ts-auth-service' , 12340),
    Service('ts-security-service' , 11188),
    Service('ts-seat-service' , 18898),
    Service('ts-route-service' , 11178),
    Service('ts-route-plan-service', 14578),
    Service('ts-rebook-service' , 18886),
    Service('ts-price-service' , 16579),
    Service('ts-preserve-service' , 14568),
    Service('ts-preserve-other-service' , 14569),
    Service('ts-payment-service' , 19001),
    Service('ts-order-service' , 12031),
    Service('ts-order-other-service' , 12032),
    Service('ts-news-service' , 12862),
    Service('ts-notification-service' , 17853),
    Service('ts-user-service' , 12342),
    Service('ts-inside-payment-service' , 18673),
    Service('ts-food-service' , 18856),
    Service('ts-food-map-service' , 18855),
    Service('ts-execute-service' , 12386),
    Service('ts-contacts-service' , 12347),
    Service('ts-consign-service' , 16111),
    Service('ts-consign-price-service', 16110),
    Service('ts-config-service' , 15679),
    Service('ts-cancel-service', 18885),
    Service('ts-basic-service' , 15680),
    Service('ts-assurance-service' , 18888),
    Service('ts-admin-user-service' , 16115),
    Service('ts-admin-travel-service' , 16114),
    Service('ts-admin-route-service' , 16113),
    Service('ts-admin-order-service' , 16112),
    Service('ts-admin-basic-info-service' , 18767)
]





