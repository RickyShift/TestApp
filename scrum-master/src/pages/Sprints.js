import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import SelectedPopup from "./SelectedPopup";
import "../styles/Sprints.css";

export default function Sprints() {
  const [sprints, setSprints] = useState([]);
  const [newSprint, setNewSprint] = useState({
    name: "",
    description: "",
    endDate: "",
    project_id: "",
  });
  const [projects, setProjects] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [selectedSprint, setSelectedSprint] = useState("");
  const [selectedProject, setSelectedProject] = useState("");
  const [editPopup, setEditPopup] = useState(false);

  useEffect(() => {
    fetch("http://localhost:8080/api/sprint")
      .then((res) => res.json())
      .then(setSprints)
      .catch((error) => console.error("Error fetching sprints:", error));

    fetch("http://localhost:8080/api/project")
      .then((res) => res.json())
      .then(setProjects)
      .catch((error) => console.error("Error fetching projects:", error));

    fetch("http://localhost:8080/api/userStory")
      .then((res) => res.json())
      .then(setTasks)
      .catch((error) => console.error("Error fetching tasks:", error));
  }, []);

  const handleCreateSprint = async () => {
    if (
      !newSprint.name ||
      !newSprint.description ||
      !newSprint.endDate ||
      !newSprint.project_id
    )
      return;
    const sprintData = {
      ...newSprint,
      startDate: new Date().toISOString().split("T")[0],
      state: "TO_DO",
    };

    try {
      const response = await fetch("http://localhost:8080/api/sprint/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(sprintData),
      });

      if (response.ok) {
        const createdSprint = await response.json();
        setSprints([...sprints, createdSprint]);
        setNewSprint({
          name: "",
          description: "",
          endDate: "",
          project_id: "",
        });
        setEditPopup(false);
      }
    } catch (error) {
      console.error("Error creating sprint:", error);
    }
  };

  return (
    <div className="app">
      <Header />
      <main className="main-content">
        <h1 className="sprint-title">Sprint Management</h1>

        <div className="sprint-controls">
          <button className="create-sprint" onClick={() => setEditPopup(true)}>
            + Create New Sprint
          </button>
          <select
            value={selectedProject}
            onChange={(e) => setSelectedProject(e.target.value)}
          >
            <option value="">Show all projects</option>
            {projects.map((project) => (
              <option key={project.id} value={project.id}>
                {project.name}
              </option>
            ))}
          </select>
          <select
            value={selectedSprint}
            onChange={(e) => setSelectedSprint(e.target.value)}
          >
            <option value="">Show all sprints</option>
            {sprints
              .filter(
                (sprint) =>
                  selectedProject === "" ||
                  sprint.project_id === selectedProject
              )
              .map((sprint) => (
                <option key={sprint.id} value={sprint.id}>
                  Sprint {sprint.id}
                </option>
              ))}
          </select>
        </div>

        <h2 className="sprint-section-title">Active Sprints</h2>
        <div className="sprint-list">
          {sprints
            .filter(
              (sprint) =>
                sprint.state !== "COMPLETED" &&
                (selectedSprint === "" ||
                  sprint.id === parseInt(selectedSprint)) &&
                (selectedProject === "" ||
                  sprint.project_id === selectedProject)
            )
            .map((sprint) => (
              <div key={sprint.id} className="sprint-card">
                <div className="sprint-header">
                  <span>Sprint {sprint.id}</span>
                  <span className="sprint-status">({sprint.state})</span>
                </div>
                <div className="sprint-actions">
                  <button className="start-sprint">▶ Start</button>
                  <button className="end-sprint">⏹ End</button>
                  <button className="sprint-btn-delete">❌</button>
                </div>
                <div className="sprint-tasks">
                  <h4>User Stories</h4>
                  {tasks
                    .filter((task) => task.sprintId === sprint.id)
                    .map((task) => (
                      <div key={task.id} className="sprint-task-item">
                        {task.name}
                      </div>
                    ))}
                </div>
              </div>
            ))}
        </div>

        <h2 className="sprint-section-title">Completed Sprints</h2>
        <div className="sprint-list">
          {sprints
            .filter(
              (sprint) =>
                sprint.state === "COMPLETED" &&
                (selectedSprint === "" ||
                  sprint.id === parseInt(selectedSprint)) &&
                (selectedProject === "" ||
                  sprint.project_id === selectedProject)
            )
            .map((sprint) => (
              <div key={sprint.id} className="sprint-card completed">
                <div className="sprint-header">
                  <span>Sprint {sprint.id}</span>
                  <span className="sprint-status">({sprint.state})</span>
                </div>
                <div className="sprint-tasks">
                  <h4>User Stories</h4>
                  {tasks
                    .filter((task) => task.sprintId === sprint.id)
                    .map((task) => (
                      <div key={task.id} className="sprint-task-item">
                        {task.name}
                      </div>
                    ))}
                </div>
              </div>
            ))}
        </div>
      </main>
    </div>
  );
}
