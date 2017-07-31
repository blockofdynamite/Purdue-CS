"""GroupsApp Views
Created by Naman Patwari on 10/10/2016.
"""
from django.shortcuts import render

from CommentsApp import models
from . import models
from . import forms
from AuthenticationApp import models as auth_models


def getGroups(request):
    if request.user.is_authenticated():
        groups_list = models.Group.objects.all()
        context = {
            'groups': groups_list,
        }
        return render(request, 'groups.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        is_member = in_group.members.filter(email__exact=request.user.email)
        context = {
            'group': in_group,
            'userIsMember': is_member,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
        }
        return render(request, 'group.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getGroupForm(request):
    if request.user.is_authenticated():
        return render(request, 'groupform.html', {'form': 'formsuccess'})
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def getGroupFormSuccess(request):
    if request.user.is_authenticated():
        if request.method == 'POST':
            form = forms.GroupForm(request.POST or None)
            if not request.user.is_student:
                return render(request, 'groupform.html', {'error': 'Error: Only students may create groups!'})
            if form.is_valid():
                if models.Group.objects.filter(name__exact=form.cleaned_data['name']).exists():
                    return render(request, 'groupform.html', {'error': 'Error: That Group name already exists!'})
                new_group = models.Group(name=form.cleaned_data['name'], description=form.cleaned_data['description'])
                new_group.save()
                context = {
                    'name': form.cleaned_data['name'],
                }
                return render(request, 'groupformsuccess.html', context)
        else:
            form = forms.GroupForm()
        return render(request, 'groupform.html', {'error': 'Oops! Something went wrong!'})
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def addMember(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_email = request.POST.get('email', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        if in_group.members.all().filter(group__members__email=request.user.email).exists():
            try:
                in_member = models.MyUser.objects.get(email__exact=in_email)
            except models.MyUser.DoesNotExist:
                return render(request, 'group.html', context={
                    'group': in_group,
                    'userIsMember': True,
                    'project': in_group.project,
                    'projects': models.Project.objects.all(),
                    'comments': in_group.comments.all(),
                    'error': 'That user doesn\'t exist!',
                })
            in_member.group_set.add(in_group)
            in_member.save()
            in_group.members.add(in_member)
            in_group.save()
            context = {
                'group': in_group,
                'userIsMember': True,
                'project': in_group.project,
                'projects': models.Project.objects.all(),
                'comments': in_group.comments.all(),
            }
            return render(request, 'group.html', context)
        else:
            return render(request, 'group.html', context={
                'group': in_group,
                'userIsMember': True,
                'project': in_group.project,
                'projects': models.Project.objects.all(),
                'comments': in_group.comments.all(),
                'error': 'You are not in this group!',
            })
    return render(request, 'autherror.html')


def joinGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        in_group.members.add(request.user)
        in_group.save()
        request.user.group_set.add(in_group)
        request.user.save()
        context = {
            'group': in_group,
            'userIsMember': True,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
        }
        return render(request, 'group.html', context)
    return render(request, 'autherror.html')


def unjoinGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        in_group.members.remove(request.user)
        in_group.save()
        request.user.group_set.remove(in_group)
        request.user.save()
        context = {
            'group': in_group,
            'userIsMember': False,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
        }
        return render(request, 'group.html', context)
    return render(request, 'autherror.html')


def selectProject(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('group', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        in_project = request.POST.get('project_picker', 'None')
        project_selected = models.Project.objects.get(name__exact=in_project)
        if in_group.members.all().filter(group__members__email=request.user.email).exists():
            in_group.project = project_selected
            in_group.save()
            context = {
                'group': in_group,
                'userIsMember': True,
                'project': in_group.project,
                'projects': models.Project.objects.all(),
                'comments': in_group.comments.all(),
            }
            return render(request, 'group.html', context)
        else:
            return render(request, 'group.html', context={
                'group': in_group,
                'userIsMember': True,
                'project': in_group.project,
                'projects': models.Project.objects.all(),
                'comments': in_group.comments.all(),
                'error': 'You are not in this group!',
            })
    return render(request, 'autherror.html')


def addComment(request):
    if request.user.is_authenticated():
        if request.method == 'POST':
            in_name = request.GET.get('group', 'None')
            in_group = models.Group.objects.get(name__exact=in_name)
            comment = request.POST.get('comment', 'None')
            if in_group.members.all().filter(group__members__email=request.user.email).exists():
                new_comment = models.Comment(user=request.user.get_full_name(), comment=comment)
                new_comment.save()
                in_group.comments.add(new_comment)
                context = {
                    'group': in_group,
                    'userIsMember': True,
                    'project': in_group.project,
                    'projects': models.Project.objects.all(),
                    'comments': in_group.comments.all(),
                }
                return render(request, 'group.html', context)
    return render(request, 'autherror.html')


def editGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)

        context = {
            "name": in_group.name,
            "desc": in_group.description,
            "group": in_group,
            'form': 'editsuccess',
        }
        return render(request, 'groupform.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def deleteGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        in_group.delete()
        groups_list = models.Group.objects.all()
        context = {
            'groups': groups_list,
        }
        return render(request, 'groups.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def editGroupSuccess(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('group', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        in_group.name = request.POST.get('name', 'None')
        in_group.description = request.POST.get('description', 'None')
        in_group.save()

        context = {
            'group': in_group,
            'userIsMember': True,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
        }
        return render(request, 'group.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def matchGroup(request):
    if request.user.is_authenticated():
        in_name = request.GET.get('name', 'None')
        in_group = models.Group.objects.get(name__exact=in_name)
        projects_list = []
        for item in models.Project.objects.all():
            score = 0
            for i in in_group.members.all():
                if not i.is_student:
                    continue
                i = auth_models.Student.objects.get(user__email=i)
                if i.java and item.language.lower().find('java') > -1:
                    score += 1
                if i.c_sharp and item.language.lower().find('c#') > -1:
                    score += 1
                if i.c_plus_plus and item.language.lower().find('c++') > -1:
                    score += 1
                if i.c and item.language.lower().find('c') > -1:
                    score += 1
                if i.php and item.language.lower().find('php') > -1:
                    score += 1
                if i.python and item.language.lower().find('python') > -1:
                    score += 1
                if i.javascript and item.language.lower().find('javascript') > -1:
                    score += 1
                if i.perl and item.language.lower().find('perl') > -1:
                    score += 1
                if i.sql and item.language.lower().find('sql') > -1:
                    score += 1
                if i.django and item.language.lower().find('django') > -1:
                    score += 1
            project = {
                'score': score,
                'years': item.years,
                'language': item.language,
                'name': item.name
            }
            projects_list.append(project)
        projects_list.sort(key=lambda x: x['score'], reverse=True)
        for item in projects_list:
            print item['score']
        context = {
            'projects': projects_list,
            'group': in_name
        }
        return render(request, 'findproject.html', context)
    # render error page if user is not logged in
    return render(request, 'autherror.html')


def markProject(request):
    in_name = request.GET.get('name', 'None')
    in_group = models.Group.objects.get(name__exact=in_name)
    in_name = request.GET.get('project', 'None')
    project_selected = models.Project.objects.get(name__exact=in_name)
    if in_group.members.all().filter(group__members__email=request.user.email).exists():
        in_group.project = project_selected
        in_group.save()
        context = {
            'group': in_group,
            'userIsMember': True,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
        }
        return render(request, 'group.html', context)
    else:
        return render(request, 'group.html', context={
            'group': in_group,
            'userIsMember': True,
            'project': in_group.project,
            'projects': models.Project.objects.all(),
            'comments': in_group.comments.all(),
            'error': 'You are not in this group!',
        })