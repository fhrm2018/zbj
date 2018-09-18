package com.qiyou.dhlive.prd.component.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.qiyou.dhlive.core.bms.outward.model.BmsResource;
import com.qiyou.dhlive.core.bms.outward.service.IBmsResourceService;
import com.qiyou.dhlive.prd.component.annotation.ResourceAnnotation;
import com.yaozhong.framework.base.common.utils.EmptyUtil;
import com.yaozhong.framework.web.mvc.controller.BaseController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.common.collect.Lists;


public class ResourceBaseController extends BaseController implements InitializingBean {
    @Autowired
    private IBmsResourceService bmsResourceService;

    public void afterPropertiesSet() throws Exception {
        registResourceByClass(getClass());
    }

    public void registResourceByClass(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        List needRetryMethods = registResource(Arrays.asList(methods));
        if ((EmptyUtil.isNotEmpty(needRetryMethods)) && (needRetryMethods.size() > 0))
            needRetryMethods = registResource(Arrays.asList(methods));
    }

    private List<Method> registResource(List<Method> methods) {
        List needRetryMethods = Lists.newArrayList();
        for (Method method : methods) {
            ResourceAnnotation resourceAnnotation = (ResourceAnnotation) method.getAnnotation(ResourceAnnotation.class);
            RequestMapping requestMapping = (RequestMapping) method.getAnnotation(RequestMapping.class);
            if ((EmptyUtil.isNotEmpty(requestMapping)) && (EmptyUtil.isNotEmpty(resourceAnnotation))) {
                BmsResource existResource = this.bmsResourceService.getResourceByName(resourceAnnotation.name(), resourceAnnotation.type());
                if (EmptyUtil.isEmpty(existResource)) {
                    BmsResource resource = new BmsResource();
                    resource.setUrl(resourceAnnotation.url());
                    resource.setName(resourceAnnotation.name());
                    resource.setType(resourceAnnotation.type());
                    resource.setIcon(resourceAnnotation.icon());
                    resource.setRemark(resourceAnnotation.remark());
                    if (EmptyUtil.isNotEmpty(resourceAnnotation.pName())) {
                        BmsResource parentResource = this.bmsResourceService.getResourceByName(resourceAnnotation.pName(), 1);
                        if (EmptyUtil.isEmpty(parentResource)) {
                            parentResource = new BmsResource();
                            parentResource.setName(resourceAnnotation.pName());
                            parentResource.setType(1);
                            parentResource = this.bmsResourceService.saveResource(parentResource);
                            resource.setParentId(parentResource.getId());
                            resource.setParentName(resourceAnnotation.pName());
                        } else {
                            resource.setParentId(parentResource.getId());
                            resource.setParentName(parentResource.getName());
                        }
                    }
                    this.bmsResourceService.saveResource(resource);
                } else {
                    existResource.setUrl(resourceAnnotation.url());
                    existResource.setName(resourceAnnotation.name());
                    existResource.setType(resourceAnnotation.type());
                    existResource.setRemark(resourceAnnotation.remark());
                    //existResource.setGroupName(resourceAnnotation.group());
                    existResource.setIcon(resourceAnnotation.icon());
                    this.bmsResourceService.saveResource(existResource);
                }
            }
        }
        return needRetryMethods;
    }
}
