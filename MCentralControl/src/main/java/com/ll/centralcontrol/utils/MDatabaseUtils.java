package com.ll.centralcontrol.utils;

import com.ll.centralcontrol.dao.MInterfaceDao;
import com.ll.centralcontrol.dao.MParamDao;
import com.ll.centralcontrol.dao.MServiceDao;
import com.ll.centralcontrol.mapper.InterfacesMapper;
import com.ll.centralcontrol.mapper.ParamsMapper;
import com.ll.centralcontrol.mapper.ServicesMapper;
import com.ll.common.service.MParamer;
import com.ll.common.service.MService;
import com.ll.common.service.MSvcInterface;
import com.ll.common.utils.MResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/5/26
 */

@Component
public class MDatabaseUtils {

    @Autowired
    private InterfacesMapper interfacesMapper;


    @Autowired
    private ParamsMapper paramsMapper;


    @Autowired
    private ServicesMapper servicesMapper;

    @Autowired
    DataSource dataSource;

    public static MDatabaseUtils databaseUtils;

    private DataSourceTransactionManager transactionManager;

    @PostConstruct
    public void init() {
        MDatabaseUtils.databaseUtils = this;
        this.transactionManager = new DataSourceTransactionManager(this.dataSource);
    }


    public void insertService(MService service) {
        MDatabaseUtils.databaseUtils.deleteService(service);
        MServiceDao serviceDao = MServiceDao.fromDto(service);
        TransactionTemplate transactionTemplate = new TransactionTemplate(databaseUtils.transactionManager);
        transactionTemplate.execute(txStatus -> {
            databaseUtils.servicesMapper.insert(serviceDao);
            for (MSvcInterface serviceInterface : service.getServiceInterfaceMap().values()) {
                MInterfaceDao interfaceDao = MInterfaceDao.fromDto(serviceInterface);
                databaseUtils.interfacesMapper.insert(interfaceDao);
                for (int i = 0; i < serviceInterface.getParams().size(); ++i) {
                    MParamDao paramDao = MParamDao.fromDto(
                            serviceInterface.getId(),
                            serviceInterface.getParams().get(i),
                            i
                    );
                    databaseUtils.paramsMapper.insert(paramDao);
                }
            }
            return null;
        });
    }


    public void deleteService(MService service) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(databaseUtils.transactionManager);
        transactionTemplate.execute(txStatus -> {
            for (MSvcInterface serviceInterface : service.getServiceInterfaceMap().values()) {
                databaseUtils.paramsMapper.deleteByInterfaceId(serviceInterface.getId());
            }
            databaseUtils.interfacesMapper.deleteByServiceId(service.getId());

            databaseUtils.servicesMapper.deleteById(service.getId());
            return null;
        });
    }


    public List<MService> getAllServices() {
        List<MService> resultList = new ArrayList<>();
        for (MServiceDao serviceDao : databaseUtils.servicesMapper.getAll()) {
            resultList.add(this.getServiceByDao(serviceDao));
        }
        return resultList;
    }

    private MService getServiceByDao(MServiceDao serviceDao) {
        MService service = serviceDao.toDto();
        List<MInterfaceDao> interfaceDaoList = databaseUtils.interfacesMapper.getByServiceId(service.getId());
        Map<String, MSvcInterface> interfaceMap = new HashMap<>();
        for (MInterfaceDao interfaceDao : interfaceDaoList) {
            MSvcInterface serviceInterface = interfaceDao.toDto();
            List<MParamDao> paramDaoList = databaseUtils.paramsMapper.getByInterfaceId(serviceInterface.getId());
            paramDaoList.sort(Comparator.comparingInt(MParamDao::getOrder));
            List<MParamer> paramerList = new ArrayList<>();
            for (MParamDao paramDao : paramDaoList) {
                paramerList.add(paramDao.toDto());
            }
            serviceInterface.setParams(paramerList);
//            interfaceMap.put(serviceInterface.getId(), serviceInterface);
            interfaceMap.put(serviceInterface.getPatternUrl(),serviceInterface);
        }
        // get dependency
        service.setServiceInterfaceMap(interfaceMap);
        return service;
    }


    public MService getMServiceById(String serviceId){
        return getServiceByDao(databaseUtils.servicesMapper.getById(serviceId));

    }

    public Set<String> getALlServiceVersionInfo(){
        List<MService> list =  databaseUtils.getAllServices();
        return list.stream().map(a -> a.getServiceId()).collect(Collectors.toSet());
    }

}
