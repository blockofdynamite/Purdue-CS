from django import forms
from django.forms import TextInput


class ProjectForm(forms.Form):
    name = forms.CharField(max_length=200)
    language = forms.CharField(max_length=200)
    years = forms.CharField(widget=TextInput(attrs={'type': 'number'}))
    specialty = forms.CharField(max_length=200)
    description = forms.CharField(max_length=200)

