"""AuthenticationApp URL Configuration

Created by Naman Patwari on 10/4/2016.
"""

from django.conf.urls import url

from . import views

urlpatterns = [
    url(r'^login$', views.auth_login, name='Login'),
    url(r'^logout$', views.auth_logout, name='Logout'),
    url(r'^register$', views.auth_register, name='Register'),
    url(r'^update$', views.update_profile, name='UpdateProfile'),
    url(r'^student$', views.view_student_profile, name='ViewStudentProfile'),
    url(r'^teacher', views.view_teacher_profile, name='ViewTeacherProfile'),
    url(r'^engineer$', views.view_engineer_profile, name='ViewEngineerProfile'),
    url(r'^user$', views.view_user_profile, name='ViewUserProfile')
]