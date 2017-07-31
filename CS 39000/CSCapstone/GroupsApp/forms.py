"""GroupsApp Forms

Created by Naman Patwari on 10/10/2016.
"""
from django import forms
from django.forms import TextInput


class GroupForm(forms.Form):
    name = forms.CharField(label='Name', max_length=30)
    description = forms.CharField(widget=TextInput(attrs={'type': 'text'}))
