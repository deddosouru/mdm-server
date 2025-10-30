package com.hmdm.plugins.settings.rest;

import com.google.inject.Inject;
import com.hmdm.plugins.settings.model.SettingsTemplate;
import com.hmdm.plugins.settings.persistence.TemplateDAO;
import com.hmdm.plugins.settings.service.SettingsService;
import com.hmdm.rest.json.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(tags = {"Settings Templates"})
@Path("/plugins/settings/templates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TemplateResource {
    
    private static final Logger log = LoggerFactory.getLogger(TemplateResource.class);
    
    private final TemplateDAO templateDAO;
    private final SettingsService settingsService;
    
    @Inject
    public TemplateResource(TemplateDAO templateDAO, SettingsService settingsService) {
        this.templateDAO = templateDAO;
        this.settingsService = settingsService;
    }

    @GET
    @Path("/")
    @ApiOperation("Получить список всех шаблонов")
    public Response getAllTemplates() {
        try {
            List<SettingsTemplate> templates = templateDAO.getAllTemplates();
            return Response.OK(templates);
        } catch (Exception e) {
            log.error("Failed to get templates", e);
            return Response.INTERNAL_ERROR();
        }
    }

    @GET
    @Path("/{id}")
    @ApiOperation("Получить шаблон по ID")
    public Response getTemplate(@PathParam("id") Long id) {
        try {
            SettingsTemplate template = templateDAO.getTemplate(id);
            if (template == null) {
                return Response.notFound("Template not found");
            }
            return Response.OK(template);
        } catch (Exception e) {
            log.error("Failed to get template " + id, e);
            return Response.INTERNAL_ERROR();
        }
    }

    @POST
    @Path("/")
    @ApiOperation("Создать новый шаблон")
    public Response createTemplate(SettingsTemplate template) {
        try {
            templateDAO.saveTemplate(template);
            return Response.OK(template);
        } catch (Exception e) {
            log.error("Failed to create template", e);
            return Response.INTERNAL_ERROR();
        }
    }

    @PUT
    @Path("/{id}")
    @ApiOperation("Обновить существующий шаблон")
    public Response updateTemplate(@PathParam("id") Long id, SettingsTemplate template) {
        try {
            if (templateDAO.getTemplate(id) == null) {
                return Response.notFound("Template not found");
            }
            template.setId(id);
            templateDAO.saveTemplate(template);
            return Response.OK(template);
        } catch (Exception e) {
            log.error("Failed to update template " + id, e);
            return Response.INTERNAL_ERROR();
        }
    }

    @DELETE
    @Path("/{id}")
    @ApiOperation("Удалить шаблон")
    public Response deleteTemplate(@PathParam("id") Long id) {
        try {
            if (templateDAO.getTemplate(id) == null) {
                return Response.notFound("Template not found");
            }
            templateDAO.deleteTemplate(id);
            return Response.OK();
        } catch (Exception e) {
            log.error("Failed to delete template " + id, e);
            return Response.INTERNAL_ERROR();
        }
    }

    @POST
    @Path("/{id}/copy")
    @ApiOperation("Создать копию шаблона")
    public Response copyTemplate(@PathParam("id") Long id) {
        try {
            SettingsTemplate original = templateDAO.getTemplate(id);
            if (original == null) {
                return Response.notFound("Template not found");
            }

            // Создаем копию
            SettingsTemplate copy = new SettingsTemplate();
            copy.setName(original.getName() + " (копия)");
            copy.setDescription(original.getDescription());
            copy.setSettings(original.getSettings());
            copy.setActive(true);
            copy.setApplyMode(original.getApplyMode());

            templateDAO.saveTemplate(copy);
            return Response.OK(copy);
        } catch (Exception e) {
            log.error("Failed to copy template " + id, e);
            return Response.INTERNAL_ERROR();
        }
    }

    @PUT
    @Path("/{id}/rename")
    @ApiOperation("Переименовать шаблон")
    public Response renameTemplate(@PathParam("id") Long id, @QueryParam("name") String newName) {
        try {
            SettingsTemplate template = templateDAO.getTemplate(id);
            if (template == null) {
                return Response.notFound("Template not found");
            }

            template.setName(newName);
            templateDAO.saveTemplate(template);
            return Response.OK(template);
        } catch (Exception e) {
            log.error("Failed to rename template " + id, e);
            return Response.INTERNAL_ERROR();
        }
    }
}