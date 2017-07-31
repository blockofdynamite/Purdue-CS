"""
ProjectsApp Views

Created by Harris Christiansen on 10/02/16.
"""
from django.shortcuts import render

from . import models
from . import forms


def getProjects(request):
    if request.user.is_authenticated():
        projects_list = models.Project.objects.all()
        context = {
            'projects': projects_list,
        }
        return render(request, 'projects.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_project = models.Project.objects.get(name__exact=in_name)
        is_member = in_project.members.filter(email__exact=request.user.email)
        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)
        context = {
            'project': in_project,
            'userIsMember': is_member,
            'bookmarked': is_bookmarked,
        }
        return render(request, 'project.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getProjectForm(request):
    if request.user.is_authenticated():
        return render(request, 'projectform.html', {'form': 'formsuccess'})
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getProjectFormSuccess(request):
    if request.user.is_authenticated():
        if request.method == 'POST':
            form = forms.ProjectForm(request.POST or request.FILES)

            if not request.user.is_engineer:
                return render(request, 'projectform.html', {'error': 'Error: You are not an engineer!'})

            elif form.is_valid():
                if models.Project.objects.filter(name__exact=form.cleaned_data['name']).exists():
                    return render(request, 'projectform.html', {'error': 'Error: That project name already exists!'})

                if request.user.companyForProject is None:
                    return render(request, 'companyform.html', {'error': 'Error: you must be part of a company!'})

                new_project = models.Project(
                    name=form.cleaned_data.get('name', None),
                    language=form.cleaned_data.get('language', None),
                    years=form.cleaned_data.get('years', None),
                    specialty=form.cleaned_data.get('specialty', None),
                    description=form.cleaned_data.get('description', None),
                    owner=request.user,
                    companyForProject=request.user.companyForProject
                )
                new_project.save()
                context = {
                    'name': form.cleaned_data['name'],
                }
                return render(request, 'projectformsuccess.html', context)
        else:
            return render(request, 'projectform.html')
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def joinProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_project = models.Project.objects.get(name__exact=in_name)
        in_project.members.add(request.user)
        in_project.save()
        request.user.project_set.add(in_project)
        request.user.save()
        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)
        context = {
            'project': in_project,
            'userIsMember': True,
            'bookmarked': is_bookmarked,
        }
        return render(request, 'project.html', context)
    return render(request, 'autherror.html')


def unjoinProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_project = models.Project.objects.get(name__exact=in_name)
        in_project.members.remove(request.user)
        in_project.save()
        request.user.project_set.remove(in_project)
        request.user.save()
        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)
        context = {
            'project': in_project,
            'userIsMember': False,
            'bookmarked': is_bookmarked
        }
        return render(request, 'project.html', context)
    return render(request, 'autherror.html')


def editProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_project = models.Project.objects.get(name__exact=in_name)

        context = {
            'project': in_project,
            'userIsMember': True,
            'form': 'editsuccess',
        }
        return render(request, 'projectform.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def deleteProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Project.objects.get(name__exact=in_name)
        in_group.delete()
        groups_list = models.Project.objects.all()
        context = {
            'projects': groups_list,
        }
        return render(request, 'projects.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def editProjectSuccess(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('project', 'None')
        in_group = models.Project.objects.get(name__exact=in_name)
        in_group.name = request.POST.get('name', 'None')
        in_group.description = request.POST.get('description', 'None')
        in_group.language = request.POST.get('language', 'None')
        in_group.years = request.POST.get('years', 'None')
        in_group.specialty = request.POST.get('specialty', 'None')

        in_group.save()

        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)

        context = {
            'project': in_group,
            'userIsMember': True,
            'bookmarked': is_bookmarked
        }
        return render(request, 'project.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def bookmark(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Project.objects.get(name__exact=in_name)

        request.user.bookmarks.add(in_group)

        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)

        context = {
            'project': in_group,
            'userIsMember': True,
            'bookmarked': is_bookmarked,
        }
        return render(request, 'project.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')

def unbookmark(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Project.objects.get(name__exact=in_name)

        request.user.bookmarks.remove(in_group)

        is_bookmarked = request.user.bookmarks.filter(name__exact=in_name)

        context = {
            'project': in_group,
            'userIsMember': True,
            'bookmarked': is_bookmarked,
        }
        return render(request, 'project.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def bookmarks(request):
    if request.user.is_authenticated():
        projects_list = request.user.bookmarks.all()
        context = {
            'projects': projects_list,
        }
        return render(request, 'projects.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')