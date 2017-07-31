from django.db import models


class Comment(models.Model):
    user = models.CharField(max_length=300, null=True)
    time = models.DateTimeField(auto_now=True)
    comment = models.CharField(max_length=500)
