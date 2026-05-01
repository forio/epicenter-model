build_epicenter <- function() {
  if (!require("devtools", character.only = T, quietly = T)) {
    install.packages("devtools", repos = "http://cran.us.r-project.org")
    library("devtools", character.only = T)
  }
  if (!require("roxygen2", character.only = T, quietly = T)) {
    devtools::install_github("klutometis/roxygen")
    library("roxygen2", character.only = T)
  }
  setwd("./Epicenter")
  document()
  setwd("..")
  build(pkg = "Epicenter", binary = F)
}

build_epicenter()