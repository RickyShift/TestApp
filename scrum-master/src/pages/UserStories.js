import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import "../styles/UserStories.css";

export default function UserStories() {
  const [tasks, setTasks] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [filter, setFilter] = useState("none");
  const [projectFilter, setProjectFilter] = useState("");
  const [sprintFilter, setSprintFilter] = useState("");
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/userStory")
      .then((res) => res.json())
      .then((result) => setTasks(result))
      .catch((error) => console.error("Error fetching tasks:", error));
  }, []);

  const handleDragStart = (e, taskId) => {
    e.dataTransfer.setData("taskId", taskId);
  };

  const handleDrop = (e, newState) => {
    const taskId = e.dataTransfer.getData("taskId");
    let updatedTask = tasks.find((task) => task.id === parseInt(taskId));

    updatedTask = { ...updatedTask, state: newState };

    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task.id === parseInt(taskId) ? updatedTask : task
      )
    );
    // Send update to backend
    fetch(`http://localhost:8080/api/userStory/update/${taskId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedTask), // Send updated task data
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("Failed to update task state");
        }
        return res.json();
      })
      .then((data) => console.log("Task updated successfully:", data))
      .catch((error) => console.error("Error updating task:", error));
  };

  const filteredTasks = tasks
    .filter((task) =>
      task.name.toLowerCase().includes(searchQuery.toLowerCase())
    )
    .filter((task) => (projectFilter ? task.project === projectFilter : true))
    .filter((task) => (sprintFilter ? task.sprint === sprintFilter : true))
    .sort((a, b) => {
      if (filter === "name") return a.name.localeCompare(b.name);
      if (filter === "priority") return b.priority - a.priority;
      return 0;
    });

  return (
    <div className="app">
      <Header />
      <main className="board-main-content">
        <h2 className="board-title">User Stories Manager</h2>

        <div className="board-controls">
          <input
            type="text"
            placeholder="Search user stories..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <select onChange={(e) => setProjectFilter(e.target.value)}>
            <option value="">All Projects</option>
            {[...new Set(tasks.map((task) => task.project))].map((project) => (
              <option key={project} value={project}>
                {project}
              </option>
            ))}
          </select>
          <select onChange={(e) => setSprintFilter(e.target.value)}>
            <option value="">All Sprints</option>
            {[...new Set(tasks.map((task) => task.sprint))].map((sprint) => (
              <option key={sprint} value={sprint}>
                {sprint}
              </option>
            ))}
          </select>
        </div>

        <div className="board-task-board">
          {["TO_DO", "IN_PROGRESS", "COMPLETED"].map((state) => (
            <div
              key={state}
              className="board-task-column"
              onDragOver={(e) => e.preventDefault()}
              onDrop={(e) => handleDrop(e, state)}
            >
              <h3>{state}</h3>
              <div className="board-task-container">
                {filteredTasks
                  .filter((task) => task.state === state)
                  .map((task) => (
                    <div
                      key={task.id}
                      className="board-task-card"
                      draggable
                      onDragStart={(e) => handleDragStart(e, task.id)}
                    >
                      <p>
                        <strong>Name: </strong> {task.name}
                      </p>
                      <p>
                        <strong>Project: </strong> {task.project}
                      </p>
                      <p>
                        <strong>Sprint: </strong> {task.sprint}
                      </p>
                      <p>
                        <strong>End Date: </strong>
                        {formatDate(task.endDate)}
                      </p>
                      <p>
                        <strong>Priority: </strong>
                        {task.priority.charAt(0).toUpperCase() +
                          task.priority.slice(1).toLowerCase()}
                      </p>
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
