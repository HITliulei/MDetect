import requests
import json
import random
import tornado.ioloop
import tornado.web
import datetime
import uuid
import ast

class Service:
    def __init__(self, serviceName, port, basic_url):
        self.serviceName = serviceName
        self.port = port
        self.base_url = basic_url

    def getbasicurl(self):
        return 'http://'+self.serviceName + ":" +str(self.port) +"/" + self.base_url

    def geturl(self, str):
        return self.getbasicurl() + "/" + str

    def __str__(self):
        return self.serviceName

# control the k8s cluster
url_apizipkin = 'http://192.168.1.102:30001/api/v2/trace/'

serviceList = {
    'ts-voucher-service' : Service('ts-voucher-service', 16101, 'api/v1/'),
    'ts-verification-code-service' : Service('ts-verification-code-service', 15678, 'api/v1/verifycode'),
    'ts-travel-service' : Service('ts-travel-service' , 12346, 'api/v1/travelservice'),                   #user
    'ts-travel-plan-service' : Service('ts-travel-plan-service', 14322, 'api/v1/travelplanservice'),      #user
    'ts-travel2-service' : Service('ts-travel2-service' , 16346, 'api/v1/travel2service'),                #user
    'ts-train-service' : Service('ts-train-service' , 14567, 'api/v1/trainservice'),
    'ts-ticketinfo-service' : Service('ts-ticketinfo-service', 15681, 'api/v1/ticketinfoservice'),        #user
    'ts-ticket-office-service' : Service('ts-ticket-office-service' , 16108, 'api/v1/'),                  #user
    'ts-station-service' : Service('ts-station-service', 12345, 'api/v1/stationservice'),
    'ts-auth-service' : Service('ts-auth-service' , 12340, 'api/v1/users'),                               #user
    'ts-security-service' : Service('ts-security-service' , 11188, 'api/v1/securityservice'),
    'ts-seat-service' : Service('ts-seat-service' , 18898, 'api/v1/seatservice'),
    'ts-route-service' : Service('ts-route-service' , 11178, 'api/v1/routeservice'),                    # user
    'ts-route-plan-service' : Service('ts-route-plan-service', 14578, 'api/v1/routeplanservice'),       # user
    'ts-rebook-service' : Service('ts-rebook-service' , 18886, 'api/v1/rebookservice'),                 # user
    'ts-price-service' : Service('ts-price-service' , 16579, 'api/v1/priceservice'),
    'ts-preserve-service' : Service('ts-preserve-service' , 14568, 'api/v1/preserveservice'),
    'ts-preserve-other-service' : Service('ts-preserve-other-service' , 14569, 'api/v1/preserveotherservice'),
    'ts-payment-service' : Service('ts-payment-service' , 19001, 'api/v1/paymentservice'),
    'ts-order-service' : Service('ts-order-service' , 12031, 'api/v1/orderservice'),
    'ts-order-other-service' : Service('ts-order-other-service' , 12032, 'api/v1/orderOtherService'),
    'ts-news-service' : Service('ts-news-service' , 12862, 'api/v1/'),
    'ts-notification-service' : Service('ts-notification-service' , 17853, 'api/v1/notifyservice'),
    'ts-user-service' : Service('ts-user-service' , 12342, 'api/v1/userservice/users'),
    'ts-inside-payment-service' : Service('ts-inside-payment-service' , 18673, 'api/v1/inside_pay_service'),
    'ts-food-service' : Service('ts-food-service' , 18856, 'api/v1/foodservice'),
    'ts-food-map-service' : Service('ts-food-map-service' , 18855, 'api/v1/foodmapservice'),
    'ts-execute-service' : Service('ts-execute-service' , 12386, 'api/v1/executeservice'),
    'ts-contacts-service' : Service('ts-contacts-service' , 12347, 'api/v1/contactservice'),
    'ts-consign-service' : Service('ts-consign-service' , 16111, 'api/v1/consignservice'),
    'ts-consign-price-service' : Service('ts-consign-price-service', 16110, 'api/v1/consignpriceservice'),
    'ts-config-service' : Service('ts-config-service' , 15679, 'api/v1/configservice'),
    'ts-cancel-service' : Service('ts-cancel-service', 18885, 'api/v1/cancelservice'),
    'ts-basic-service' : Service('ts-basic-service' , 15680, 'api/v1/basicservice'),
    'ts-assurance-service' : Service('ts-assurance-service' , 18888, 'api/v1/assuranceservice'),
    'ts-admin-user-service' : Service('ts-admin-user-service' , 16115, 'api/v1/adminuserservice/users'), #admin
    'ts-admin-travel-service' : Service('ts-admin-travel-service' , 16114, 'api/v1/admintravelservice'), #admin
    'ts-admin-route-service' : Service('ts-admin-route-service' , 16113, 'api/v1/adminrouteservice'), # admin
    'ts-admin-order-service' : Service('ts-admin-order-service' , 16112, 'api/v1/adminorderservice'), # admin
    'ts-admin-basic-info-service' : Service('ts-admin-basic-info-service' , 18767, 'api/v1/adminbasicservice') # admin
}

serviceList1 = {}
for i in serviceList:
    serviceList1[i] = [serviceList[i]]
serviceList1['ts-travels-service'] = Service('ts-travels-service', 12346, 'api/v1/travelsservice')
serviceList1['ts-travels2-service'] = Service('ts-travels2-service', 16346, 'api/v1/travels2service')

stations = [{'id': 'shanghai', 'name': 'Shang Hai', 'stayTime': 10}, {'id': 'shanghaihongqiao', 'name': 'Shang Hai Hong Qiao', 'stayTime': 10}, {'id': 'taiyuan', 'name': 'Tai Yuan', 'stayTime': 5}, {'id': 'beijing', 'name': 'Bei Jing', 'stayTime': 10}, {'id': 'nanjing', 'name': 'Nan Jing', 'stayTime': 8}, {'id': 'shijiazhuang', 'name': 'Shi Jia Zhuang', 'stayTime': 8}, {'id': 'xuzhou', 'name': 'Xu Zhou', 'stayTime': 7}, {'id': 'jinan', 'name': 'Ji Nan', 'stayTime': 5}, {'id': 'hangzhou', 'name': 'Hang Zhou', 'stayTime': 9}, {'id': 'jiaxingnan', 'name': 'Jia Xing Nan', 'stayTime': 2}, {'id': 'zhenjiang', 'name': 'Zhen Jiang', 'stayTime': 2}, {'id': 'wuxi', 'name': 'Wu Xi', 'stayTime': 3}, {'id': 'suzhou', 'name': 'Su Zhou', 'stayTime': 3}]
trains = [{'id': 'GaoTieOne', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 250}, {'id': 'GaoTieTwo', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 200}, {'id': 'DongCheOne', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 180}, {'id': 'ZhiDa', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 120}, {'id': 'TeKuai', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 120}, {'id': 'KuaiSu', 'economyClass': 50, 'confortClass': 50, 'averageSpeed': 90}]
routes = [{'id': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'stations': ['shanghai', 'nanjing', 'shijiazhuang', 'taiyuan'], 'distances': [0, 350, 1000, 1300], 'startStationId': 'shanghai', 'terminalStationId': 'taiyuan'}, {'id': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'stations': ['nanjing', 'xuzhou', 'jinan', 'beijing'], 'distances': [0, 500, 700, 1200], 'startStationId': 'nanjing', 'terminalStationId': 'beijing'}, {'id': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'stations': ['taiyuan', 'shijiazhuang', 'nanjing', 'shanghai'], 'distances': [0, 300, 950, 1300], 'startStationId': 'taiyuan', 'terminalStationId': 'shanghai'}, {'id': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'stations': ['shanghai', 'taiyuan'], 'distances': [0, 1300], 'startStationId': 'shanghai', 'terminalStationId': 'taiyuan'}, {'id': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'stations': ['shanghaihongqiao', 'jiaxingnan', 'hangzhou'], 'distances': [0, 150, 300], 'startStationId': 'shanghaihongqiao', 'terminalStationId': 'hangzhou'}, {'id': '92708982-77af-4318-be25-57ccb0ff69ad', 'stations': ['nanjing', 'zhenjiang', 'wuxi', 'suzhou', 'shanghai'], 'distances': [0, 100, 150, 200, 250], 'startStationId': 'nanjing', 'terminalStationId': 'shanghai'}, {'id': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'stations': ['nanjing', 'shanghai'], 'distances': [0, 250], 'startStationId': 'nanjing', 'terminalStationId': 'shanghai'}, {'id': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'stations': ['nanjing', 'suzhou', 'shanghai'], 'distances': [0, 200, 250], 'startStationId': 'nanjing', 'terminalStationId': 'shanghai'}, {'id': '084837bb-53c8-4438-87c8-0321a4d09917', 'stations': ['suzhou', 'shanghai'], 'distances': [0, 50], 'startStationId': 'suzhou', 'terminalStationId': 'shanghai'}, {'id': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'stations': ['shanghai', 'suzhou'], 'distances': [0, 50], 'startStationId': 'shanghai', 'terminalStationId': 'suzhou'}]
travel_data = [{0: {'trip_data': {'loginId': 'admin', 'tripId': 'G837', 'trainTypeId': 'GaoTieTwo', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'nanjing', 'shijiazhuang', 'taiyuan']", 'terminalStationId': 'shijiazhuang', 'startingTime': '2020-12-22T00:17:36.542143', 'endTime': '2020-12-22T05:17:36.542143'}, 'cost_time': 300}}, {1: {'trip_data': {'loginId': 'admin', 'tripId': 'G554', 'trainTypeId': 'GaoTieTwo', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'shijiazhuang']", 'terminalStationId': 'shijiazhuang', 'startingTime': '2020-12-21T22:17:36.542172', 'endTime': '2020-12-22T01:32:36.542172'}, 'cost_time': 195}}, {2: {'trip_data': {'loginId': 'admin', 'tripId': 'K659', 'trainTypeId': 'KuaiSu', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'startingStationId': 'xuzhou', 'stationsId': "['nanjing', 'xuzhou', 'jinan', 'beijing']", 'terminalStationId': 'jinan', 'startingTime': '2020-12-21T22:17:36.542185', 'endTime': '2020-12-22T00:30:36.542185'}, 'cost_time': 133}}, {3: {'trip_data': {'loginId': 'admin', 'tripId': 'Z191', 'trainTypeId': 'ZhiDa', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'startingStationId': 'xuzhou', 'stationsId': "['xuzhou', 'jinan']", 'terminalStationId': 'jinan', 'startingTime': '2020-12-21T23:17:36.542196', 'endTime': '2020-12-22T00:57:36.542196'}, 'cost_time': 100}}, {4: {'trip_data': {'loginId': 'admin', 'tripId': 'G283', 'trainTypeId': 'GaoTieOne', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'startingStationId': 'taiyuan', 'stationsId': "['taiyuan', 'shijiazhuang', 'nanjing', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-22T00:17:36.542206', 'endTime': '2020-12-22T05:29:36.542206'}, 'cost_time': 312}}, {5: {'trip_data': {'loginId': 'admin', 'tripId': 'T568', 'trainTypeId': 'TeKuai', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'startingStationId': 'taiyuan', 'stationsId': "['taiyuan', 'shijiazhuang', 'nanjing', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-22T00:17:36.542218', 'endTime': '2020-12-22T11:07:36.542218'}, 'cost_time': 650}}, {6: {'trip_data': {'loginId': 'admin', 'tripId': 'T628', 'trainTypeId': 'TeKuai', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'startingStationId': 'taiyuan', 'stationsId': "['taiyuan', 'shijiazhuang', 'nanjing']", 'terminalStationId': 'nanjing', 'startingTime': '2020-12-21T22:17:36.542229', 'endTime': '2020-12-22T06:12:36.542229'}, 'cost_time': 475}}, {7: {'trip_data': {'loginId': 'admin', 'tripId': 'K700', 'trainTypeId': 'KuaiSu', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'taiyuan']", 'terminalStationId': 'taiyuan', 'startingTime': '2020-12-21T22:17:36.542239', 'endTime': '2020-12-22T12:43:36.542239'}, 'cost_time': 866}}, {8: {'trip_data': {'loginId': 'admin', 'tripId': 'D318', 'trainTypeId': 'DongCheOne', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'taiyuan']", 'terminalStationId': 'taiyuan', 'startingTime': '2020-12-21T22:17:36.542249', 'endTime': '2020-12-22T05:30:36.542249'}, 'cost_time': 433}}, {9: {'trip_data': {'loginId': 'admin', 'tripId': 'D454', 'trainTypeId': 'DongCheOne', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'taiyuan']", 'terminalStationId': 'taiyuan', 'startingTime': '2020-12-21T22:17:36.542259', 'endTime': '2020-12-22T05:30:36.542259'}, 'cost_time': 433}}, {10: {'trip_data': {'loginId': 'admin', 'tripId': 'Z867', 'trainTypeId': 'ZhiDa', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'startingStationId': 'shanghaihongqiao', 'stationsId': "['shanghaihongqiao', 'jiaxingnan', 'hangzhou']", 'terminalStationId': 'hangzhou', 'startingTime': '2020-12-21T23:17:36.542269', 'endTime': '2020-12-22T01:47:36.542269'}, 'cost_time': 150}}, {11: {'trip_data': {'loginId': 'admin', 'tripId': 'T446', 'trainTypeId': 'TeKuai', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'startingStationId': 'shanghaihongqiao', 'stationsId': "['shanghaihongqiao', 'jiaxingnan', 'hangzhou']", 'terminalStationId': 'hangzhou', 'startingTime': '2020-12-21T23:17:36.542278', 'endTime': '2020-12-22T01:47:36.542278'}, 'cost_time': 150}}, {12: {'trip_data': {'loginId': 'admin', 'tripId': 'Z557', 'trainTypeId': 'ZhiDa', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'startingStationId': 'shanghaihongqiao', 'stationsId': "['shanghaihongqiao', 'jiaxingnan']", 'terminalStationId': 'jiaxingnan', 'startingTime': '2020-12-22T00:17:36.542288', 'endTime': '2020-12-22T01:32:36.542288'}, 'cost_time': 75}}, {13: {'trip_data': {'loginId': 'admin', 'tripId': 'D724', 'trainTypeId': 'DongCheOne', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'startingStationId': 'zhenjiang', 'stationsId': "['nanjing', 'zhenjiang', 'wuxi', 'suzhou', 'shanghai']", 'terminalStationId': 'wuxi', 'startingTime': '2020-12-21T23:17:36.542297', 'endTime': '2020-12-21T23:33:36.542297'}, 'cost_time': 16}}, {14: {'trip_data': {'loginId': 'admin', 'tripId': 'G72', 'trainTypeId': 'GaoTieOne', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'zhenjiang', 'wuxi', 'suzhou']", 'terminalStationId': 'suzhou', 'startingTime': '2020-12-22T00:17:36.542307', 'endTime': '2020-12-22T01:05:36.542307'}, 'cost_time': 48}}, {15: {'trip_data': {'loginId': 'admin', 'tripId': 'K255', 'trainTypeId': 'KuaiSu', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'startingStationId': 'zhenjiang', 'stationsId': "['zhenjiang', 'wuxi']", 'terminalStationId': 'wuxi', 'startingTime': '2020-12-22T00:17:36.542317', 'endTime': '2020-12-22T00:50:36.542317'}, 'cost_time': 33}}, {16: {'trip_data': {'loginId': 'admin', 'tripId': 'D364', 'trainTypeId': 'DongCheOne', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-22T00:17:36.542327', 'endTime': '2020-12-22T01:40:36.542327'}, 'cost_time': 83}}, {17: {'trip_data': {'loginId': 'admin', 'tripId': 'T297', 'trainTypeId': 'TeKuai', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-21T22:17:36.542338', 'endTime': '2020-12-22T00:22:36.542338'}, 'cost_time': 125}}, {18: {'trip_data': {'loginId': 'admin', 'tripId': 'T234', 'trainTypeId': 'TeKuai', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-21T23:17:36.542348', 'endTime': '2020-12-22T01:22:36.542348'}, 'cost_time': 125}}, {19: {'trip_data': {'loginId': 'admin', 'tripId': 'K407', 'trainTypeId': 'KuaiSu', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'startingStationId': 'nanjing', 'stationsId': "['nanjing', 'suzhou', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-21T22:17:36.542357', 'endTime': '2020-12-22T01:03:36.542357'}, 'cost_time': 166}}, {20: {'trip_data': {'loginId': 'admin', 'tripId': 'T93', 'trainTypeId': 'TeKuai', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'startingStationId': 'suzhou', 'stationsId': "['suzhou', 'shanghai']", 'terminalStationId': 'shanghai', 'startingTime': '2020-12-21T23:17:36.542368', 'endTime': '2020-12-21T23:42:36.542368'}, 'cost_time': 25}}, {21: {'trip_data': {'loginId': 'admin', 'tripId': 'Z925', 'trainTypeId': 'ZhiDa', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'suzhou']", 'terminalStationId': 'suzhou', 'startingTime': '2020-12-21T22:17:36.542378', 'endTime': '2020-12-21T22:42:36.542378'}, 'cost_time': 25}}, {22: {'trip_data': {'loginId': 'admin', 'tripId': 'T778', 'trainTypeId': 'TeKuai', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'suzhou']", 'terminalStationId': 'suzhou', 'startingTime': '2020-12-22T00:17:36.542388', 'endTime': '2020-12-22T00:42:36.542388'}, 'cost_time': 25}}, {23: {'trip_data': {'loginId': 'admin', 'tripId': 'K474', 'trainTypeId': 'KuaiSu', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'startingStationId': 'shanghai', 'stationsId': "['shanghai', 'suzhou']", 'terminalStationId': 'suzhou', 'startingTime': '2020-12-21T23:17:36.542399', 'endTime': '2020-12-21T23:50:36.542399'}, 'cost_time': 33}}]
price_train_route = [{'id': '426d9494-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.44, 'firstClassPriceRate': 1.0}, {'id': '426d9495-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.33, 'firstClassPriceRate': 1.0}, {'id': '426d9496-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.56, 'firstClassPriceRate': 1.0}, {'id': '426d9497-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.42, 'firstClassPriceRate': 1.0}, {'id': '426d9498-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.07, 'firstClassPriceRate': 1.0}, {'id': '426d9499-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '0b23bd3e-876a-4af3-b920-c50a90c90b04', 'basicPriceRate': 0.57, 'firstClassPriceRate': 1.0}, {'id': '426d949a-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.2, 'firstClassPriceRate': 1.0}, {'id': '426d949b-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.6, 'firstClassPriceRate': 1.0}, {'id': '426d949c-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.04, 'firstClassPriceRate': 1.0}, {'id': '426d949d-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.57, 'firstClassPriceRate': 1.0}, {'id': '426d949e-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.79, 'firstClassPriceRate': 1.0}, {'id': '426d949f-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '9fc9c261-3263-4bfa-82f8-bb44e06b2f52', 'basicPriceRate': 0.62, 'firstClassPriceRate': 1.0}, {'id': '426d94a0-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.65, 'firstClassPriceRate': 1.0}, {'id': '426d94a1-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.38, 'firstClassPriceRate': 1.0}, {'id': '426d94a2-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.96, 'firstClassPriceRate': 1.0}, {'id': '426d94a3-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.21, 'firstClassPriceRate': 1.0}, {'id': '426d94a4-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.92, 'firstClassPriceRate': 1.0}, {'id': '426d94a5-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': 'd693a2c5-ef87-4a3c-bef8-600b43f62c68', 'basicPriceRate': 0.63, 'firstClassPriceRate': 1.0}, {'id': '426d94a6-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.5, 'firstClassPriceRate': 1.0}, {'id': '426d94a7-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.95, 'firstClassPriceRate': 1.0}, {'id': '426d94a8-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.0, 'firstClassPriceRate': 1.0}, {'id': '426d94a9-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.73, 'firstClassPriceRate': 1.0}, {'id': '426d94aa-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.67, 'firstClassPriceRate': 1.0}, {'id': '426d94ab-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '20eb7122-3a11-423f-b10a-be0dc5bce7db', 'basicPriceRate': 0.9, 'firstClassPriceRate': 1.0}, {'id': '426d94ac-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.59, 'firstClassPriceRate': 1.0}, {'id': '426d94ad-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.57, 'firstClassPriceRate': 1.0}, {'id': '426d94ae-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.56, 'firstClassPriceRate': 1.0}, {'id': '426d94af-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.24, 'firstClassPriceRate': 1.0}, {'id': '426d94b0-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.78, 'firstClassPriceRate': 1.0}, {'id': '426d94b1-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '1367db1f-461e-4ab7-87ad-2bcc05fd9cb7', 'basicPriceRate': 0.92, 'firstClassPriceRate': 1.0}, {'id': '426d94b2-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.47, 'firstClassPriceRate': 1.0}, {'id': '426d94b3-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.4, 'firstClassPriceRate': 1.0}, {'id': '426d94b4-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.82, 'firstClassPriceRate': 1.0}, {'id': '426d94b5-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.17, 'firstClassPriceRate': 1.0}, {'id': '426d94b6-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.29, 'firstClassPriceRate': 1.0}, {'id': '426d94b7-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '92708982-77af-4318-be25-57ccb0ff69ad', 'basicPriceRate': 0.96, 'firstClassPriceRate': 1.0}, {'id': '426d94b8-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.01, 'firstClassPriceRate': 1.0}, {'id': '426d94b9-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.06, 'firstClassPriceRate': 1.0}, {'id': '426d94ba-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.72, 'firstClassPriceRate': 1.0}, {'id': '426d94bb-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.97, 'firstClassPriceRate': 1.0}, {'id': '426d94bc-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.46, 'firstClassPriceRate': 1.0}, {'id': '426d94bd-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': 'aefcef3f-3f42-46e8-afd7-6cb2a928bd3d', 'basicPriceRate': 0.86, 'firstClassPriceRate': 1.0}, {'id': '426d94be-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.29, 'firstClassPriceRate': 1.0}, {'id': '426d94bf-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.12, 'firstClassPriceRate': 1.0}, {'id': '426d94c0-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.52, 'firstClassPriceRate': 1.0}, {'id': '426d94c1-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.62, 'firstClassPriceRate': 1.0}, {'id': '426d94c2-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.67, 'firstClassPriceRate': 1.0}, {'id': '426d94c3-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': 'a3f256c1-0e43-4f7d-9c21-121bf258101f', 'basicPriceRate': 0.11, 'firstClassPriceRate': 1.0}, {'id': '426d94c4-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.74, 'firstClassPriceRate': 1.0}, {'id': '426d94c5-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.57, 'firstClassPriceRate': 1.0}, {'id': '426d94c6-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.19, 'firstClassPriceRate': 1.0}, {'id': '426d94c7-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.46, 'firstClassPriceRate': 1.0}, {'id': '426d94c8-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.99, 'firstClassPriceRate': 1.0}, {'id': '426d94c9-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': '084837bb-53c8-4438-87c8-0321a4d09917', 'basicPriceRate': 0.58, 'firstClassPriceRate': 1.0}, {'id': '426d94ca-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieOne', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.22, 'firstClassPriceRate': 1.0}, {'id': '426d94cb-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'GaoTieTwo', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.13, 'firstClassPriceRate': 1.0}, {'id': '426d94cc-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'DongCheOne', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.03, 'firstClassPriceRate': 1.0}, {'id': '426d94cd-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'ZhiDa', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.07, 'firstClassPriceRate': 1.0}, {'id': '426d94ce-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'TeKuai', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.76, 'firstClassPriceRate': 1.0}, {'id': '426d94cf-307d-11eb-9b8e-af7fe23a8d86', 'trainType': 'KuaiSu', 'routeId': 'f3d4d4ef-693b-4456-8eed-59c0d717dd08', 'basicPriceRate': 0.75, 'firstClassPriceRate': 1.0}]

# login to get the token
url_getToken = 'http://ts-auth-service:12340/api/v1/users/login'

# create
url_createUser = 'http://ts-admin-user-service:16115/api/v1/adminuserservice/users'
# create connect
url_createConnect = 'http://ts-admin-basic-info-service:18767/api/v1/adminbasicservice/adminbasic/contacts'
# create inside_payment
url_createInside = 'http://ts-inside-payment-service:18673/api/v1/inside_pay_service/inside_payment/account'
''''''
# route control, get: get info, post: insert route
url_getrouteinfo= 'http://ts-admin-route-service:16113/api/v1/adminrouteservice/adminroute'
# get train info
url_train = 'http://ts-train-service:14567/api/v1/trainservice/trains'
# travel control, get: get info, post, add travel
url_travel = 'http://ts-admin-travel-service:16114/api/v1/admintravelservice/admintravel'
# create price
url_createPrice = 'http://ts-admin-basic-info-service:18767/api/v1/adminbasicservice/adminbasic/prices'


ip_master = "172.31.43.94"
coll = 'http://'+ip_master+':32223/api/v1/collectdata'
k8s_url = 'http://'+ip_master+':32222/api/v1/controlk8s'
class Login:
    def __init__(self, username, password, ver, headers):
        self.__url = serviceList['ts-auth-service'].geturl('login')
        if ver == None:
            self.__data  = {"username": "admin", "password": "222222"}
        else:
            self.__data = {"username": username, "password": password, "verificationCode": ver}
        req = requests.post(self.__url, json=self.__data, headers=headers)
        self.__res = json.loads(req.text)['data']
        self.__token = json.loads(req.text)['data']['token']

    def getHeaders(self):
        heads = {
            # 'Content-Type': 'applon/json',
            'Authorization': 'Bearer ' + self.__token
        }
        return heads
    def getResponse_data(self):
        return self.__res


# 需求队列，不进行更改的
commands = []
def dorequests(i, ex_name, ex_times):
    commands_ofThis_user = commands[i]
    print("请求队列为" + str(commands[i]))
    adminLogin = Login('admin', '222222', None,
                       {'traceId': commands_ofThis_user['adminuuid'], 'ex_name': ex_name, 'ex_times': ex_times})
    nowTime = str(datetime.datetime.now()).replace(' ', 'T')
    username = commands_ofThis_user['username']
    trip_data = commands_ofThis_user['theTrip_data']
    tripId = trip_data['tripId']
    startStationsId = commands_ofThis_user['startStation']
    endStationsId = commands_ofThis_user['endStation']
    startStationsName = commands_ofThis_user['startStationName']
    endStationsName = commands_ofThis_user['endStationName']
    travel_base = None
    order_base = None
    order_base_name = None
    if str(tripId)[0] =='G' or str(tripId)[0] == 'D':
        travel_base = serviceList['ts-travel-service']
        order_base = serviceList['ts-order-service']
        order_base_name = 'order'
    else:
        travel_base = serviceList['ts-travel2-service']
        order_base = serviceList['ts-order-other-service']
        order_base_name = 'orderOther'
    # 1. login
    password = '111111'
    userLogin = Login(username, password, '1234', {'traceId': commands_ofThis_user['loginuuId'],  'ex_name': ex_name, 'ex_times': ex_times})
    header_users = userLogin.getHeaders()
    header_users['ex_name'] = ex_name
    header_users['ex_times'] = ex_times
    headers = adminLogin.getHeaders()
    headers['ex_name'] = ex_name
    headers['ex_times'] = ex_times
    res = userLogin.getResponse_data()
    if res['username'] != username:
        return
    userId = res['userId'] #用户的uuid
    print(" 登陆成功， userId为 ： " + userId)

    # 2. get connacts
    headers['traceId'] = commands_ofThis_user['getConnactsuuid']
    res_connacts = json.loads(requests.get(serviceList['ts-contacts-service'].geturl('contacts/account/' + userId), headers=headers).text)
    connact = res_connacts['data'][0]
    print("获取联系人成功：， 联系人为： " + str(res_connacts))

    # 3. 监测黄牛
    headers['traceId'] = commands_ofThis_user['checkscalperuuid']
    res_checkout = json.loads(requests.get(serviceList['ts-security-service'].geturl('securityConfigs/' + userId), headers=headers).text)
    if res_checkout['status'] == 1:
        print("监测黄牛成功: " + str(res_checkout))
    # 4. 查看trip
    headers['traceId'] = commands_ofThis_user['seeTripuuid']
    TripInfo = {
        'startingPlace' : startStationsName,
        'endPlace' : endStationsName,
        'departureTime': str(trip_data['startingTime'])
    }
    res_seeTrip = requests.post(
        url=travel_base.geturl('trips/left'),
        headers=headers,
        json=TripInfo)
    print("plt.legend() : " + str(res_seeTrip.text))

    # 计划
    headers['traceId'] = commands_ofThis_user['strategy']['strategyuuid']
    res_seeStrategy = requests.post(
        serviceList['ts-travel-plan-service'].geturl(commands_ofThis_user['strategy']['url']),
        headers=headers,
        json=TripInfo)
    print("查看策略成功 : " + str(res_seeStrategy.text))

    # 5. 查看此trip的详细情况
    TripAllDetailInfo = {
        'tripId': tripId,
        # 'travelDate': nowTime,
        'travelDate': str(trip_data['startingTime']),
        'from': startStationsName,
        'to': endStationsName
    }
    headers['traceId'] = commands_ofThis_user['seeThisTrip']
    res_seeThisTripDetail = json.loads(requests.post(
        url=travel_base.geturl('trip_detail'),
        headers=headers,
        json=TripAllDetailInfo).text)
    print("查看详细情况： " + str(res_seeThisTripDetail))
    TripDetail = res_seeThisTripDetail['data']
    # 6. buy ticket
    orderuuid = commands_ofThis_user['orderuuid']
    # 6.1 get ticket
    headers['traceId'] =commands_ofThis_user['getTicketInfouuid']
    res_getTickerInfo = requests.post(
        url=serviceList['ts-ticketinfo-service'].geturl('ticketinfo'),
        headers=headers,
        json={
            'trip' : trip_data,
            'startingPlace' : startStationsName,
            'endPlace' : endStationsName,
            'departureTime' : str(trip_data['startingTime'])
        })
    print("获取票价信息成功: " + str(res_getTickerInfo.text))
    TravelResult = json.loads(res_getTickerInfo.text)['data']

    # 6.2 get seat
    headers['traceId'] = commands_ofThis_user['seatuuid']
    res_getSeat = requests.post(
        url=serviceList['ts-seat-service'].geturl('seats'),
        headers=headers,
        json={
            'travelDate': str(trip_data['startingTime']),
            'trainNumber': tripId,
            'startStation': startStationsId,
            'destStation': endStationsId,
            'seatType': i % 2 + 2
        }
    )

    print("获取座位成功： " + str(res_getSeat.text))
    # 6.3 create order
    headers['traceId'] = commands_ofThis_user['createOrderuuid']
    price = 'economyClass'
    if i%2 + 2 == 2:
        price = 'confortClass'

    order = {
        'id': orderuuid,
        'boughtDate' : nowTime,
        'travelDate' : str(trip_data['startingTime']),
        'travelTime' : TripDetail['tripResponse']['startingTime'],
        'accountId' : userId,
        'contactsName' : connact['name'],
        'documentType' : connact['documentType'],
        'contactsDocumentNumber': connact['documentNumber'],
        'trainNumber' : tripId,
        'seatClass' : i%2 + 2,
        'seatNumber' : commands_ofThis_user['seatNumber'],
        'from' : endStationsId,
        'to' : startStationsId,
        'status' : 0,
        'price' : TravelResult['prices'][price]
    }
    res_buyorder = requests.post(
        url=order_base.geturl(order_base_name),
        headers=headers,
        json=order)
    print("创造订单成功：" + str(res_buyorder.text))
    # if json.loads(res_buyorder.text)['status'] == 1:

    ResponseOrder = json.loads(res_buyorder.text)['data']
    orderuuid = str(ResponseOrder['id'])
    # 6.4 pay for order
    headers['traceId'] = commands_ofThis_user['payforOrder']
    res_payfororder = requests.get(
        url=order_base.geturl(order_base_name+"/orderPay/" + orderuuid),
        headers=headers
    )
    print("付款成功 " + str(res_payfororder.text))
    # 插入cancel 服务，
    if commands_ofThis_user['canceluuid'] != None:
        headers['traceId'] = commands_ofThis_user['canceluuid']
        print("canceluuid headers : " + str(headers))
        res_cancel = requests.get(
            url=serviceList['ts-cancel-service'].geturl('cancel/'+orderuuid + "/" + userId),
            headers=headers)
        print("插入取消微服务成功：" + str(res_cancel.text))
        return
    # 7. 保障服务
    if commands_ofThis_user['buyinsuranceuuid'] != None:
        # headers['traceId'] = commands_ofThis_user['buyinsuranceuuid']
        header_users['traceId'] = commands_ofThis_user['buyinsuranceuuid']
        res_buyinsure = requests.get(
            url=serviceList['ts-assurance-service'].geturl('assurances/1/'+str(orderuuid)),
            headers=header_users)
        print("购买保障服务成功:" + str(res_buyinsure.text))
    # 8. 托运服务
    if commands_ofThis_user['consignuuid'] != None:
        header_users['traceId'] = commands_ofThis_user['consignuuid']
        res_consign = requests.post(
            url=serviceList['ts-consign-service'].geturl('consigns'),
            headers=header_users,
            json={
                'id' : commands_ofThis_user['consignuuid'],
                'orderId' : orderuuid,
                'accountId' : userId,
                'handleDate' : str(datetime.datetime.now() + datetime.timedelta(hours=+5)),
                'targetDate' : str(ResponseOrder['travelDate']),
                'from' : str(ResponseOrder['from']),
                'to': str(ResponseOrder['to']),
                'consignee': '',
                'phone': '',
                'weight': 10.0,
                'isWithin': True,
            }
        )

        print("托运服务成功： " + str(res_consign.text))
    # 9. food服务
    if commands_ofThis_user['fooduuid'] != None:
        headers['traceId'] = commands_ofThis_user['fooduuid']
        print("fooduuid headers : " + str(headers))
        res_foodrequest = requests.post(
            url=serviceList['ts-food-service'].geturl('orders'),
            headers=headers,
            json={
                'id': commands_ofThis_user['fooduuid'],
                'orderId': orderuuid,
                'foodType': 2,
                'stationName': endStationsName,
                'storeName': 'KFC',
                'foodName': 'Hamburger',
                'price': 5.0
            }
        )

        print("购买food需求成功：" + str(res_foodrequest.text))

    # 插入 rebook服务
    if commands_ofThis_user['rebookuuid'] != None:
        k = 2
        if i%2+2 == 2:
            k = 3
        headers['traceId'] = commands_ofThis_user['rebookuuid']
        print("rebookuuid headers : " + str(headers))
        RebookInfo = {
            'loginId': userId,
            'orderId' : orderuuid,
            'oldTripId' : tripId,
            'tripId' : tripId,
            'seatType' : k,
            'Date': str(datetime.datetime.now()).replace(' ', 'T')
        }
        res_rebook = requests.post(
            url=serviceList['ts-rebook-service'].geturl('rebook'),headers=headers,json=RebookInfo)

        print("插入 rebook服务成功 ： " + str(res_rebook.text))
    # 10 监票服务
    headers['traceId'] = commands_ofThis_user['collectuuid']
    print("collectuuid headers : " + str(headers))
    res_collect = requests.get(
        url=serviceList['ts-execute-service'].geturl('execute/collected/' + orderuuid),
        headers=headers)

    print("插入监票服务成功：" + str(res_collect.text))

    # 11.  乘车
    headers['traceId'] = commands_ofThis_user['executeuuid']
    print("executeuuid headers : " + str(headers))
    res_exe = requests.get(
        url=serviceList['ts-execute-service'].geturl('execute/execute/' + orderuuid),
        headers=headers
    )
    print("乘车服务成功： " + str(res_exe.text))
    print("第 ：" + str(i) + "个用户的需求请求成功")


def createCommand():
    # todo
    # 生成一个需求队列，在n此部署结构中不更改, 在此对应1000个用户进行相应的
    # 生成需求的同时，生成traceId，表示此次请求的生成id
    #清洗一下 travel data
    h = 0
    for q in travel_data:
        q[h]['trip_data'].pop('loginId')
        h = h + 1
    tripNUm = 0
    j = 1
    seatNumber = 1
    for i in range(0, 1000):
        commands_ofThis_user = {}
        commands_ofThis_user['username'] = 'microserivce_userName' + str(i)
        if (i + 1) > (j * 50):
            j = j+1
            tripNUm = tripNUm + 1
            seatNumber = 1
        theTrip_data = travel_data[tripNUm][tripNUm]['trip_data']
        commands_ofThis_user['theTrip_data'] = theTrip_data
        # 设置随机+起始地点以及 随机的终点
        stationsId = ast.literal_eval(theTrip_data['stationsId'])
        startId = random.randint(0, len(stationsId) - 2)
        startStation = stationsId[startId]
        a = True
        endStationId = 0
        while a:
            h = random.randint(startId + 1, len(stationsId) - 1)
            if h != startId:
                endStationId = h
                a = False

        endStation = stationsId[endStationId]
        commands_ofThis_user['startStation'] = startStation
        commands_ofThis_user['endStation'] = endStation

        for station in stations:
            if station['id'] == startStation:
                commands_ofThis_user['startStationName'] = station['name']

        for station in stations:
            if station['id'] == endStation:
                commands_ofThis_user['endStationName'] = station['name']

        # 1. 登陆，获取userId
        commands_ofThis_user['adminuuid'] = str(uuid.uuid1())
        loginuuId = str(uuid.uuid1())
        commands_ofThis_user['loginuuId'] = loginuuId
        # 2. 获取联系人
        getConnactsuuid =str(uuid.uuid1())
        commands_ofThis_user['getConnactsuuid'] = getConnactsuuid
        # 3. 监测是否是黄牛
        checkscalperuuid = str(uuid.uuid1())
        commands_ofThis_user['checkscalperuuid'] = checkscalperuuid
        # 4. 查看所有的车
        seeTripuuid = str(uuid.uuid1())
        commands_ofThis_user['seeTripuuid'] = seeTripuuid
        strategy = random.randint(0, 2)
        strategyuuid = str(uuid.uuid1())

        if strategy == 0:
            commands_ofThis_user['strategy'] = {'strategyuuid': strategyuuid, 'url': 'travelPlan/cheapest'}
        elif strategy == 1:
            commands_ofThis_user['strategy'] = {'strategyuuid': strategyuuid, 'url': 'travelPlan/quickest'}
        else:
            commands_ofThis_user['strategy'] = {'strategyuuid': strategyuuid, 'url': 'travelPlan/minStation'}

        # 5. 查看此trip的详细信息
        seeThisTrip = str(uuid.uuid1())
        commands_ofThis_user['seeThisTrip'] = seeThisTrip

        # 6. send the order request and set the order information
        orderuuid = str(uuid.uuid1())
        commands_ofThis_user['orderuuid'] = orderuuid

        # 6.1 get ticket
        getTicketInfouuid = str(uuid.uuid1())
        commands_ofThis_user['getTicketInfouuid'] = getTicketInfouuid

        # 6.2 get seat
        seatuuid = str(uuid.uuid1())
        commands_ofThis_user['seatuuid'] = seatuuid

        # 6.3 create order
        createOrderuuid = str(uuid.uuid1())
        commands_ofThis_user['createOrderuuid'] = createOrderuuid
        commands_ofThis_user['seatNumber'] = seatNumber
        seatNumber = seatNumber + 1

        # 6.4 pay for order
        commands_ofThis_user['payforOrder'] = str(uuid.uuid1())

        # 7. 保障服务：
        if random.randint(0, 1) == 0:
            commands_ofThis_user['buyinsuranceuuid'] = str(uuid.uuid1())
        else:
            commands_ofThis_user['buyinsuranceuuid'] = None

        # 8. consign 服务
        a_consign = random.randint(0, 2)
        if a_consign != 0:
            commands_ofThis_user['consignuuid'] = None
        else:
            commands_ofThis_user['consignuuid'] = str(uuid.uuid1())
        a_food = random.randint(0, 3)
        # 9. food   服务
        if a_food == 0:
            commands_ofThis_user['fooduuid'] = str(uuid.uuid1())
        else:
            commands_ofThis_user['fooduuid'] = None

        # 10. collect 监票服务
        commands_ofThis_user['collectuuid'] = str(uuid.uuid1())

        # 11. 乘车服务
        commands_ofThis_user['executeuuid'] = str(uuid.uuid1())

        #  cancel 服务
        if random.randint(0, 15) == 0:
            commands_ofThis_user['canceluuid'] = str(uuid.uuid1())
        else:
            commands_ofThis_user['canceluuid'] = None
        #  rebook 服务
        a_rebook = random.randint(0, 10)
        if a_rebook == 0:
            commands_ofThis_user['rebookuuid'] = str(uuid.uuid1())
        else:
            commands_ofThis_user['rebookuuid'] = None
        commands.append(commands_ofThis_user)


# web app : create user, users' connect, users' inside_payment
def createUsersAndConnects(name, times):
    adminLogin = Login('admin', '222222', None, {'ex_name':name, 'ex_times':times})
    heads = adminLogin.getHeaders()
    heads['ex_name'] = name
    heads['ex_times'] = times
    for i in range(0, 1000):
        user_data = {
            # 'userId': uuid.uuid1(),
            'userName': 'microserivce_userName' + str(i),
            'password': '111111',
            'gender': random.randint(0, 1),
            'documentType': 1,
            'documentNum': str(int(random.random()*1000000)) + str(int(random.random()*10000000)) + 'X',
            'email': 'microserivce_userName' + str(i) + "@163.com"
        }
        print("%d 's people is registered", i)
        q = requests.post(url_createUser, json=user_data, headers=heads)
        q_data = json.loads(q.text)
        if json.loads(q.text)['status'] == 1:
            # 联系人生成
            connect_data = {
                'id' : str(uuid.uuid1()),
                'accountId' : q_data['data']['userId'],
                'name' : 'Contacts_One' + str(i),
                'documentType' : 1,
                'documentNumber' : 'DocumentNumber_One' + str(i),
                'phoneNumber' : 'ContactsPhoneNum_One' + str(i)
            }
            q1 = requests.post(url_createConnect, json=connect_data, headers=heads)
            print("%d 's people's connect is registered", str(q1.text))
            q1.close()
           # inside_payment生成
            inside_payment_data = {
                'userId' : q_data['data']['userId'],
                'money' : '10000',
            }
            q2 = requests.post(url_createInside, json=inside_payment_data, headers=heads)
            print("%d 's people's insidepayment is registered", str(q2.text))
            q2.close()
            q.close()


# web app 创造travel
def createTravel(name, times):
    adminLogin = Login('admin', '222222', None, {'ex_name':name, 'ex_times':times})
    heads = adminLogin.getHeaders()
    heads['ex_name'] = name
    heads['ex_times'] = times
    h = 0
    now = datetime.datetime.now()
    datechange = {}
    for i in travel_data:
        trip_data = i[h]['trip_data']
        cost_time = i[h]['cost_time']
        h = h+1
        # 更改日期即可
        a = now + datetime.timedelta(hours=+random.randint(2, 4))
        startTime = a + datetime.timedelta(days=1)
        # startTime = now + datetime.timedelta(hours=+random.randint(2, 4))
        endTime = startTime + datetime.timedelta(minutes=+cost_time)
        trip_data['startingTime'] = str(startTime).replace(' ', 'T')
        trip_data['endTime'] = str(endTime).replace(' ', 'T')
        # 添加行程
        addTravel = requests.post(url=url_travel, headers=heads, json=trip_data)
        print(addTravel.text)
        datechange[trip_data['tripId']] = {'startingTime':trip_data['startingTime'], 'endTime':trip_data['endTime']}

    print("开始更新需求数据")
    for i in range(0, 1000):
        j = commands[i]
        theTrip_data = j['theTrip_data']
        if theTrip_data['tripId'] not in datechange.keys():
            print("不在datachange当中")
            continue
        theTrip_data['startingTime'] = datechange[theTrip_data['tripId']]['startingTime']
        theTrip_data['endTime'] = datechange[theTrip_data['tripId']]['endTime']
        j['theTrip_data'] = theTrip_data
        commands[i] = j
        print(commands[i]['theTrip_data'])
    # 全部数据更新完毕
    for i in range(1, 7):
        port = 32002 + i
        requests.get("http://"+ip_master+":"+str(port)+"/updateCommand", json={'data':str(commands)})
    print("更新需求数据完毕")


# web app创造 每个火车在每条路线上的价格
def create_price_train(name, times):
    adminLogin = Login('admin', '222222', None, {'ex_name':name, 'ex_times':times})
    heads = adminLogin.getHeaders()
    heads['ex_name'] = name
    heads['ex_times'] = times
    for i in price_train_route:
        add_price = requests.post(url_createPrice, headers=heads, json=i)


class InitDataUser(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        head = self.request.headers
        name = head['name']
        times = head['times']
        # print("初始化user and connnects" + str(name) + "_" +str(times))
        createUsersAndConnects(name, times)
        # print("初始化user成功")


class InitDataTravel(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        head = self.request.headers
        name = head['name']
        times = head['times']
        print("创造travel中" + str(name) + "_" +str(times))
        createTravel(name, times)
        print("创造travel成功")


class InitDataPrice(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        head = self.request.headers
        name = head['name']
        times = head['times']
        print("初始化price" + str(name) + "_" +str(times))
        create_price_train(name, times)
        print("初始化price成功")


class givecommand(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        headers = self.request.headers
        i = headers['i']
        ex_name = headers['ex_name']
        ex_times = headers['ex_times']
        dorequests(int(i), ex_name, ex_times)


class updateCommand(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        body = self.request.body
        data = body.decode('utf-8')
        time_commands = ast.literal_eval(json.loads(data)['data'])
        # headers = self.request.headers
        # ex_name = headers['ex_name']
        # ex_times = headers['ex_times']
        # q = requests.get(coll + "/getCommand/" + str(ex_name) + "/" + str(ex_times))
        # c = json.loads(q.text)['command']
        # time_commands = ast.literal_eval(c)
        for i in range(0, 1000):
            j = commands[i]
            theTrip_data = j['theTrip_data']
            theTrip_data['startingTime'] = time_commands[i]['theTrip_data']['startingTime']
            theTrip_data['endTime'] = time_commands[i]['theTrip_data']['endTime']
            j['theTrip_data'] = theTrip_data
            commands[i] = j
        print("更新完毕")


class RequestForCircle(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        headers = self.request.headers
        i = int(headers['i'])
        ex_name = headers['ex_name']
        ex_times = headers['ex_times']

        # 请求
        commands_ofThis_user = commands[i]
        print("请求队列为" + str(commands[i]))
        adminLogin = Login('admin', '222222', None,
                           {'traceId': commands_ofThis_user['adminuuid'], 'ex_name': ex_name, 'ex_times': ex_times})
        nowTime = str(datetime.datetime.now()).replace(' ', 'T')
        username = commands_ofThis_user['username']
        trip_data = commands_ofThis_user['theTrip_data']
        tripId = trip_data['tripId']
        startStationsId = commands_ofThis_user['startStation']
        endStationsId = commands_ofThis_user['endStation']
        startStationsName = commands_ofThis_user['startStationName']
        endStationsName = commands_ofThis_user['endStationName']
        travel_base = None
        order_base = None
        order_base_name = None
        if str(tripId)[0] == 'G' or str(tripId)[0] == 'D':
            travel_base = serviceList['ts-travel-service']
            order_base = serviceList['ts-order-service']
            order_base_name = 'order'
        else:
            travel_base = serviceList['ts-travel2-service']
            order_base = serviceList['ts-order-other-service']
            order_base_name = 'orderOther'

        # 1. login
        password = '111111'
        userLogin = Login(username, password, '1234',
                          {'traceId': commands_ofThis_user['loginuuId'], 'ex_name': ex_name, 'ex_times': ex_times})
        header_users = userLogin.getHeaders()
        header_users['ex_name'] = ex_name
        header_users['ex_times'] = ex_times
        headers = adminLogin.getHeaders()
        headers['ex_name'] = ex_name
        headers['ex_times'] = ex_times
        res = userLogin.getResponse_data()
        if res['username'] != username:
            return
        userId = res['userId']  # 用户的uuid
        print(" 登陆成功， userId为 ： " + userId)

        # 4. 查看trip
        headers['traceId'] = commands_ofThis_user['seeTripuuid']
        TripInfo = {
            'startingPlace': startStationsName,
            'endPlace': endStationsName,
            'departureTime': str(trip_data['startingTime'])
        }
        res_seeTrip = requests.post(
            url=travel_base.geturl('trips/left'),
            headers=headers,
            json=TripInfo)
        print("plt.legend() : " + str(res_seeTrip.text))






class welcome(tornado.web.RequestHandler):
    def get(self, *args, **kwargs):
        print("welcome")

def make_app():
    return tornado.web.Application([
        (r"/welcome", welcome),
        (r"/giveCommand", givecommand),
        (r"/initDataUser", InitDataUser),
        (r"/initDataTravel", InitDataTravel),
        (r"/initDataPrice", InitDataPrice),
        (r"/updateCommand", updateCommand),
        (r"/requestCircle", RequestForCircle)
    ])


if __name__ == '__main__':
    filename = 'a.txt'
    f = open(filename, 'r')
    commands = ast.literal_eval(f.read())
    app = make_app()
    app.listen(32002)
    tornado.ioloop.IOLoop.current().start()
    # createCommand()
    # filename = 'a.txt'
    # with open(filename, 'w') as name:
    #     name.write(str(commands))
    #     name.close()
