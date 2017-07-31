"""ProjectsApp URL Configuration

Created by Harris Christiansen on 10/02/16.
"""
from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^project/all$', views.getProjects, name='Projects'),
    url(r'^project$', views.getProject, name='Project'),
    url(r'^project/form$', views.getProjectForm, name='ProjectForm'),
    url(r'^project/formsuccess$', views.getProjectFormSuccess, name='ProjectFormSuccess'),
    url(r'^project/join$', views.joinProject, name='ProjectCompany'),
    url(r'^project/unjoin$', views.unjoinProject, name='ProjectCompany'),
    url(r'^project/delete$', views.deleteProject, name='ProjectCompany'),
    url(r'^project/edit$', views.editProject, name='EditProject'),
    url(r'^project/editsuccess$', views.editProjectSuccess, name='EditProjectSuccess'),
    url(r'^project/bookmark$', views.bookmark, name='BookmarkProject'),
    url(r'^project/unbookmark$', views.unbookmark, name='UnbookmarkProject'),
    url(r'^bookmarks$', views.bookmarks, name='ViewBookmarks'),
]
