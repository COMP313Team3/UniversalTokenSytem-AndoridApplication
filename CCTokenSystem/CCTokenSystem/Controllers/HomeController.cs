﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CCTokenSystem.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Title = "Home Page";

            return View();
        }
        public ActionResult Student()
        {
            ViewBag.Title = "Student Page";

            return View();
        }
        public ActionResult Campus()
        {
            ViewBag.Title = "Campus Page";

            return View();
        }
        public ActionResult Department()
        {
            ViewBag.Title = "Department Page";

            return View();
        }
        public ActionResult Advisor()
        {
            ViewBag.Title = "Advisor Page";

            return View();
        }
        public ActionResult Token()
        {
            ViewBag.Title = "Token Page";

            return View();
        }
    }
}
