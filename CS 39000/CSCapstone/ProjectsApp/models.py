"""ProjectsApp Models

Created by Harris Christiansen on 10/02/16.
"""
from django.db import models
from AuthenticationApp.models import MyUser


class Project(models.Model):
    name = models.CharField(max_length=200)
    description = models.CharField(max_length=10000)
    companyForProject = models.TextField(max_length=200, null=True, blank=True)
    language = models.CharField(max_length=200, null=True, blank=True)
    specialty = models.TextField(max_length=10000, null=True, blank=True)
    years = models.CharField(max_length=10, null=True, blank=True)
    members = models.ManyToManyField(MyUser)
    owner = models.CharField(max_length=200, null=True, blank=True)

    def __str__(self):
        return self.name
