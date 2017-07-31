"""AuthenticationApp Views

Created by Naman Patwari on 10/4/2016.
"""

from django.contrib.auth import authenticate, login, logout
from django.http import HttpResponseRedirect, HttpResponse
from django.contrib.auth.decorators import login_required
from django.shortcuts import render
from django.contrib import messages
from django.template import Context, Template


from .forms import *
from .models import *


# Auth Views

def auth_login(request):
    form = LoginForm(request.POST or None)
    next_url = request.GET.get('next')
    if next_url is None:
        next_url = "/"
    if form.is_valid():
        email = form.cleaned_data['email']
        password = form.cleaned_data['password']
        user = authenticate(email=email, password=password)
        if user is not None:
            messages.success(request, 'Success! Welcome, ' + (user.first_name or ""))
            login(request, user)
            return HttpResponseRedirect(next_url)
        else:
            messages.warning(request, 'Invalid username or password.')

    context = {
        "form": form,
        "page_name": "Login",
        "button_value": "Login",
        "links": ["register"],
    }
    return render(request, 'auth_form.html', context)


def auth_logout(request):
    logout(request)
    messages.success(request, 'Success, you are now logged out')
    return render(request, 'index.html')


def auth_register(request):
    if request.user.is_authenticated():
        return HttpResponseRedirect("/")

    form = RegisterForm(request.POST or None)
    if form.is_valid():
        new_user = MyUser.objects.create_user(email=form.cleaned_data['email'],
                                              password=form.cleaned_data["password2"],
                                              first_name=form.cleaned_data["first_name"],
                                              last_name=form.cleaned_data["last_name"])
        new_user.save()

        choice = form.cleaned_data['choice']

        if choice == 'Student':
            # setting the user attirubte to be as a student
            new_user.is_student = True
            new_user.save()
            # creating the student object with the default attributes, put it as NONE first as you need to initialise it
            new_student = Student(user=new_user)
            new_student.save()
        elif choice == 'Engineer':
            new_user.is_engineer = True
            new_user.save()
            new_engineer = Engineer(user=new_user)
            new_engineer.save()
        elif choice == 'Teacher':
            new_user.is_teacher = True
            new_user.save()
            new_teacher = Teacher(user=new_user)
            new_teacher.save()

        login(request, new_user)
        messages.success(request, 'Success! Your account was created.')
        return render(request, 'index.html')

    context = {
        "form": form,
        "page_name": "Register",
        "button_value": "Register",
        "links": ["login"],
    }
    return render(request, 'auth_form.html', context)


@login_required
def update_profile(request):
    form = UpdateForm(request.POST or None, instance=request.user)

    if request.user.is_engineer:
        form_specific = EngineerUpdateForm(request.POST or None, instance=request.user)
    elif request.user.is_teacher:
        form_specific = TeacherUpdateForm(request.POST or None, instance=request.user)
    else:
        form_specific = StudentUpdateForm(request.POST or None, instance=request.user)

    if form.is_valid() and (form_specific is None or form_specific.is_valid()):
        form.save()
        if request.user.is_engineer:
            engineer = Engineer.objects.get(user__email=form.cleaned_data.get('email'))
            engineer.about = form_specific.cleaned_data.get('about')
            engineer.contact_info = form_specific.cleaned_data.get('contact_info')
            engineer.alma_mater = form_specific.cleaned_data.get('alma_mater')
            engineer.save()
        elif request.user.is_teacher:
            teacher = Teacher.objects.get(user__email=form.cleaned_data.get('email'))
            teacher.university = form_specific.cleaned_data.get('university')
            teacher.contact_info = form_specific.cleaned_data.get('contact_info')
            teacher.save()
        else:
            student = Student.objects.get(user__email=form.cleaned_data.get('email'))
            student.java = request.POST.get('java') == u'on'
            student.c_sharp = request.POST.get('c_sharp') == u'on'
            student.c_plus_plus = request.POST.get('c_plus_plus') == u'on'
            student.c = request.POST.get('c') == u'on'
            student.php = request.POST.get('php') == u'on'
            student.python = request.POST.get('python') == u'on'
            student.javascript = request.POST.get('javascript') == u'on'
            student.perl = request.POST.get('perl') == u'on'
            student.sql = request.POST.get('sql') == u'on'
            student.django = request.POST.get('django') == u'on'
            student.save()
        # if form_specific is not None:
        #    form_specific.save()
        messages.success(request, 'Success, your profile was saved!')

    context = {
        "form": form,
        "form_specific": form_specific,
        "page_name": "Update",
        "button_value": "Update",
        "links": ["logout"],
    }
    return render(request, 'auth_form.html', context)


def view_student_profile(request):
    user_email = request.GET.get('email', 'None')
    student = Student.objects.get(user__email=user_email)
    params = {
        'name': student.get_full_name(),
        'email': student,
        'java': student.java,
        'c_sharp': student.c_sharp,
        'c_plus_plus': student.c_plus_plus,
        'c': student.c,
        'php': student.php,
        'python': student.python,
        'javascript': student.javascript,
        'perl': student.perl,
        'sql': student.sql,
        'django': student.django,
    }
    return render(request, 'student.html', params)


def view_teacher_profile(request):
    user_email = request.GET.get('email', 'None')
    teacher = Teacher.objects.get(user__email=user_email)
    params = {
        'name': teacher.get_full_name(),
        'email': teacher,
        'university': teacher.university,
        'contact': teacher.contact_info
    }
    return render(request, 'teacher.html', params)


def view_engineer_profile(request):
    user_email = request.GET.get('email', 'None')
    engineer = Engineer.objects.get(user__email=user_email)
    params = {
        'name': engineer.get_full_name(),
        'email': engineer,
        'alma': engineer.alma_mater,
        'about': engineer.about,
        'contact': engineer.contact_info
    }
    return render(request, 'engineer.html', params)


def view_user_profile(request):
    if request.user.is_teacher:
        return view_teacher_profile(request)
    elif request.user.is_engineer:
        return view_engineer_profile(request)
    else:
        return view_student_profile(request)