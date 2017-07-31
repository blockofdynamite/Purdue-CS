"""
CompaniesApp Views

Created by Jacob Dunbar on 10/2/2016.
"""
from django.shortcuts import render
from django.utils.datastructures import MultiValueDictKeyError

from . import models
from . import forms


def getCompanies(request):
    if request.user.is_authenticated():
        companies_list = models.Company.objects.all()
        context = {
            'companies': companies_list,
        }
        return render(request, 'companies.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getCompany(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_company = models.Company.objects.get(name__exact=in_name)
        is_member = in_company.members.filter(email__exact=request.user.email)
        context = {
            'company': in_company,
            'userIsMember': is_member,
        }
        return render(request, 'company.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getCompanyForm(request):
    if request.user.is_authenticated():
        return render(request, 'companyform.html', {'form': 'formsuccess'})
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getCompanyFormSuccess(request):
    if request.user.is_authenticated():
        if request.method == 'POST':
            form = forms.CompanyForm(request.POST, request.FILES)

            if not request.user.is_engineer:
                return render(request, 'companyform.html', {'error': 'Error: You are not an engineer!'})

            elif form.is_valid():
                if models.Company.objects.filter(name__exact=form.cleaned_data['name']).exists():
                    return render(request, 'companyform.html', {'error': 'Error: That company name already exists!'})
                new_company = models.Company(name=form.cleaned_data['name'],
                                             photo=request.FILES['photo'],
                                             description=form.cleaned_data['description'],
                                             website=form.cleaned_data['website'])
                new_company.save()
                context = {
                    'name': form.cleaned_data['name'],
                }
                return render(request, 'companyformsuccess.html', context)
            else:
                return render(request, 'companyform.html', {'error': 'Error: Photo upload failed!'})
        else:
            form = forms.CompanyForm()
        return render(request, 'companyform.html')
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def joinCompany(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_company = models.Company.objects.get(name__exact=in_name)

        if not request.user.is_engineer:
            context = {
                'company': in_company,
                'userIsMember': True,
                'error': 'Error: You are not an engineer!'
            }
            return render(request, 'company.html', context)

        in_company.members.add(request.user)
        in_company.save()
        request.user.company_set.add(in_company)
        request.user.companyForProject = in_company.name
        request.user.save()
        context = {
            'company': in_company,
            'userIsMember': True,
        }
        return render(request, 'company.html', context)
    return render(request, 'autherror.html')


def unjoinCompany(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_company = models.Company.objects.get(name__exact=in_name)
        in_company.members.remove(request.user)
        in_company.save()
        request.user.company_set.remove(in_company)
        request.user.companyForProject = None
        request.user.save()
        context = {
            'company': in_company,
            'userIsMember': False,
        }
        return render(request, 'company.html', context)
    return render(request, 'autherror.html')


def editCompany(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_company = models.Company.objects.get(name__exact=in_name)

        context = {
            'company': in_company,
            'form': 'editsuccess',
        }
        return render(request, 'companyform.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')

def deleteCompany(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Company.objects.get(name__exact=in_name)
        in_group.delete()
        groups_list = models.Company.objects.all()
        context = {
            'companies': groups_list,
        }
        return render(request, 'companies.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def editCompanySuccess(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('company', 'None')
        in_group = models.Company.objects.get(name__exact=in_name)
        in_group.name = request.POST.get('name', 'None')
        in_group.description = request.POST.get('description', 'None')
        in_group.website = request.POST.get('website', 'None')

        try:
            in_group.photo = request.FILES['photo']
        except MultiValueDictKeyError:
            print("no photo")

        in_group.save()

        context = {
            'company': in_group,
            'userIsMember': True,
        }
        return render(request, 'company.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')
