# -*- coding: utf-8 -*-
# Generated by Django 1.10 on 2016-12-06 17:18
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Comment',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('user', models.CharField(max_length=300, null=True)),
                ('time', models.DateTimeField(auto_now=True)),
                ('comment', models.CharField(max_length=500)),
            ],
        ),
    ]
