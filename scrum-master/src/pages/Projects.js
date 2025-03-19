import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import SelectedPopup from "./SelectedPopup";
import "../styles/Projects.css";
import { FaCheckCircle } from "react-icons/fa";

export default function Projects() {
  const [projects, setProjects] = useState([]);
  const [newProject, setNewProject] = useState({
    name: "",
    description: "",
    endDate: "",
  });
  const [selectedPopup, setSelectedPopup] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [editPopup, setEditPopup] = useState(false);
  const [editedProject, setEditedProject] = useState({});

  useEffect(() => {
    fetch("http://localhost:8080/api/project")
      .then((res) => res.json())
      .then((result) => {
        const updatedProjects = result.map((project) => {
          if (
            new Date(project.endDate) < new Date() &&
            project.state !== "COMPLETED"
          ) {
            return { ...project, state: "COMPLETED" };
          }
          return project;
        });
        setProjects(updatedProjects);
      })
      .catch((error) => console.error("Error fetching projects:", error));
  }, []);

  const handleCompleteProject = async (event, id) => {
    event.stopPropagation();
    try {
      const response = await fetch(
        `http://localhost:8080/api/project/changeState/${id}?newState=COMPLETED`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
        }
      );

      if (response.ok) {
        setProjects(
          projects.map((proj) =>
            proj.id === id ? { ...proj, state: "COMPLETED" } : proj
          )
        );
      }
    } catch (error) {
      console.error("Error updating project:", error);
    }
  };

  const handleCreateProject = async () => {
    if (!newProject.name || !newProject.description || !newProject.endDate)
      return;
    const newEntry = {
      ...newProject,
      startDate: new Date().toISOString().split("T")[0],
      state: "IN_PROGRESS",
    };

    try {
      const response = await fetch("http://localhost:8080/api/project/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newEntry),
      });

      if (response.ok) {
        const createdProject = await response.json();
        setProjects([...projects, createdProject]);
        setNewProject({
          name: "",
          description: "",
          endDate: "",
        });
      }
    } catch (error) {
      console.error("Error creating project:", error);
    }
  };

  const handleDropProject = async (event, id) => {
    event.stopPropagation();
    try {
      const response = await fetch(
        `http://localhost:8080/api/project/delete/${id}`,
        {
          method: "DELETE",
        }
      );

      if (response.ok) {
        setProjects(projects.filter((project) => project.id !== id));
      } else {
        console.error("Failed to delete project");
      }
    } catch (error) {
      console.error("Error deleting project:", error);
    }
  };

  const handleSelectedProject = (project) => {
    setSelectedProject(project);
    setSelectedPopup(true);
  };

  const handleEditProject = (event, project) => {
    event.stopPropagation();
    setSelectedProject(project);
    setEditedProject({ ...project });
    setEditPopup(true);
  };

  const handleSaveChanges = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/project/update/${editedProject.id}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(editedProject),
        }
      );

      if (response.ok) {
        setProjects(
          projects.map((proj) =>
            proj.id === editedProject.id ? editedProject : proj
          )
        );
        setEditPopup(false);
      }
    } catch (error) {
      console.error("Error updating project:", error);
    }
  };

  return (
    <div className="app">
      <Header />

      <div className="new-project-container">
        <h2 className="main-title">Projects Manager</h2>

        {/* New Project Form */}
        <div className="new-project">
          <h3 className="new-project-title">New Project</h3>
          <div className="new-project-form-group">
            <input
              type="text"
              placeholder="Project Name"
              className="new-project-input-field"
              value={newProject.name}
              onChange={(e) =>
                setNewProject({ ...newProject, name: e.target.value })
              }
              required
            />
            <input
              type="text"
              placeholder="Project Description"
              className="new-project-input-field"
              value={newProject.description}
              onChange={(e) =>
                setNewProject({ ...newProject, description: e.target.value })
              }
              required
            />
            <input
              type="date"
              className="new-project-input-field"
              value={newProject.endDate}
              onChange={(e) =>
                setNewProject({ ...newProject, endDate: e.target.value })
              }
              required
            />
            <button
              className="new-project-create-button"
              onClick={handleCreateProject}
            >
              Create
            </button>
          </div>
        </div>

        {/* Project Backlog Table */}
        <div class="new-project-container">
          <h2 class="project-backlog-title">Project Backlog</h2>
          <ul class="project-backlog-responsive-table">
            <li class="project-backlog-table-header">
              <div class="project-backlog-col col-1">Project Name</div>
              <div class="project-backlog-col col-2">Project Description</div>
              <div class="project-backlog-col col-3">Start Date</div>
              <div class="project-backlog-col col-4">End Date</div>
              <div class="project-backlog-col col-5">Status</div>
              <div class="project-backlog-col col-6">Actions</div>
            </li>
            <div class="project-backlog-container">
              {projects.map((project, index) => (
                <li
                  key={index}
                  class="project-backlog-table-row"
                  onClick={() => handleSelectedProject(project)}
                >
                  <div
                    class="project-backlog-col col-1"
                    data-label="Project Name"
                  >
                    {project.name.length > 15
                      ? project.name.substring(0, 15) + "..."
                      : project.name}
                  </div>
                  <div
                    class="project-backlog-col col-2"
                    data-label="Project Description"
                  >
                    {project.description.length > 20
                      ? project.description.substring(0, 20) + "..."
                      : project.description}
                  </div>
                  <div
                    class="project-backlog-col col-3"
                    data-label="Start Date"
                  >
                    {project.startDate.split("T")[0]}
                  </div>
                  <div class="project-backlog-col col-4" data-label="End Date">
                    {project.endDate.split("T")[0]}
                  </div>
                  <div class="project-backlog-col col-5" data-label="Status">
                    {project.state}
                  </div>
                  <div class="col col-6 action-buttons" data-label="Actions">
                    <button
                      className="project-backlog-edit-button"
                      onClick={(e) => handleEditProject(e, project)}
                    >
                      Edit
                    </button>
                    <button
                      className="project-backlog-drop-button"
                      onClick={(e) => handleDropProject(e, project.id)}
                    >
                      Drop
                    </button>
                    <button
                      className="project-backlog-complete-button"
                      onClick={(e) => handleCompleteProject(e, project.id)}
                    >
                      <FaCheckCircle className="project-backlog-complete-icon" />
                    </button>
                  </div>
                </li>
              ))}
            </div>
          </ul>
        </div>
      </div>
      <SelectedPopup trigger={selectedPopup} setTrigger={setSelectedPopup}>
        {selectedProject && (
          <div>
            <p>
              <strong>Name:</strong> {selectedProject.name}
            </p>
            <p>
              <strong>Description:</strong> {selectedProject.description}
            </p>
            <p>
              <strong>Start Date:</strong>{" "}
              {selectedProject.startDate.split("T")[0]}
            </p>
            <p>
              <strong>End Date:</strong> {selectedProject.endDate.split("T")[0]}
            </p>
            <p>
              <strong>Status:</strong> {selectedProject.state}
            </p>
          </div>
        )}
      </SelectedPopup>

      <SelectedPopup trigger={editPopup} setTrigger={setEditPopup}>
        {editedProject && (
          <div>
            <h3 className="edit-popup">Edit Project</h3>
            <input
              type="text"
              className="popup-input"
              value={editedProject.name || ""}
              onChange={(e) =>
                setEditedProject({ ...editedProject, name: e.target.value })
              }
            />
            <input
              type="text"
              className="popup-input"
              value={editedProject.description || ""}
              onChange={(e) =>
                setEditedProject({
                  ...editedProject,
                  description: e.target.value,
                })
              }
            />
            <input
              type="date"
              className="popup-input"
              value={editedProject.endDate || ""}
              onChange={(e) =>
                setEditedProject({ ...editedProject, endDate: e.target.value })
              }
            />
            <div className="save-btn-container">
              <button className="save-btn" onClick={handleSaveChanges}>
                Save
              </button>
            </div>
          </div>
        )}
      </SelectedPopup>
    </div>
  );
}
