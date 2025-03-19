import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import SelectedPopup from "./SelectedPopup";
import "../styles/Backlog.css";

export default function Backlog() {
  const [userStories, setUserStories] = useState([]);
  const [projects, setProjects] = useState([]);
  const [selectedPopup, setSelectedPopup] = useState(false);
  const [selectedUserStory, setSelectedUserStory] = useState(null);
  const [editPopup, setEditPopup] = useState(false);
  const [editedUserStory, setEditedUserStory] = useState({});
  const [selectedProjectFilter, setSelectedProjectFilter] = useState("");
  const [newUserStory, setNewUserStory] = useState({
    name: "",
    description: "",
    priority: "",
    endDate: "",
    projectId: "",
    collaborators: [],
    sprintId: null,
  });
  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  const priorityOrder = {
    HIGH: 1,
    MEDIUM: 2,
    LOW: 3,
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/project")
      .then((res) => res.json())
      .then((result) => setProjects(result))
      .catch((error) => console.error("Error fetching projects:", error));
  }, []);

  useEffect(() => {
    fetch("http://localhost:8080/api/userStory") // Fetch all user stories first
      .then((res) => res.json())
      .then((stories) => {
        // Ensure we only fetch project details for valid project IDs
        const fetchProjects = stories.map((story) => {
          if (story.id !== undefined && story.id !== null) {
            return fetch(
              `http://localhost:8080/api/userStory/project/${story.id}`
            )
              .then((res) => res.json())
              .then((project) => ({ ...story, project })) // Attach project data
              .catch((error) => {
                console.error(
                  `Error fetching project for story ${story.id}:`,
                  error
                );
                return { ...story, project: null }; // If fetch fails, assign null
              });
          } else {
            return { ...story, project: null }; // If no projectId, set project to null
          }
        });

        return Promise.all(fetchProjects);
      })
      .then((storiesWithProjects) => {
        // Filter out IN_PROGRESS user stories
        const filteredStories = storiesWithProjects.filter(
          (story) =>
            story.state !== "IN_PROGRESS" || story.state !== "COMPLETED"
        );
        setUserStories(filteredStories);
      })
      .catch((error) => console.error("Error fetching user stories:", error));
  }, []);

  const handleCreateUserStory = async () => {
    if (
      !newUserStory.name ||
      !newUserStory.description ||
      !newUserStory.priority ||
      !newUserStory.endDate ||
      !newUserStory.projectId
    )
      return;

    const newEntry = {
      ...newUserStory,
      startDate: new Date().toISOString().split("T")[0],
      state: "TO_DO",
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/userStory/create",
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(newEntry),
        }
      );

      if (response.ok) {
        const createdUserStory = await response.json();
        const projectResponse = await fetch(
          `http://localhost:8080/api/userStory/project/${createdUserStory.id}`
        );
        const project = await projectResponse.json();
        const updatedUserStory = { ...createdUserStory, project };
        setUserStories([...userStories, updatedUserStory]);
        setNewUserStory({
          name: "",
          description: "",
          priority: "",
          endDate: "",
          projectId: "",
          collaborators: [],
          sprintId: null,
        });
      }
    } catch (error) {
      console.error("Error creating user story:", error);
    }
  };

  const handleDropUserStory = async (event, id) => {
    event.stopPropagation();
    try {
      const response = await fetch(
        `http://localhost:8080/api/userStory/delete/${id}`,
        {
          method: "DELETE",
        }
      );
      if (response.ok) {
        setUserStories(userStories.filter((userStory) => userStory.id !== id));
      }
    } catch (error) {
      console.error("Error deleting user story:", error);
    }
  };

  const handleEditUserStory = (event, userStory) => {
    event.stopPropagation();
    setSelectedUserStory(userStory);
    setEditedUserStory({ ...userStory });
    setEditPopup(true);
  };

  const handleSaveChanges = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/userStory/update/${editedUserStory.id}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(editedUserStory),
        }
      );
      if (response.ok) {
        setUserStories(
          userStories.map((story) =>
            story.id === editedUserStory.id ? editedUserStory : story
          )
        );
        setEditPopup(false);
      }
    } catch (error) {
      console.error("Error updating user story:", error);
    }
  };

  const activeProjects = projects.filter((project) =>
    userStories.some(
      (story) =>
        story.project &&
        story.project.id === project.id &&
        story.state !== "IN_PROGRESS" &&
        story.state !== "COMPLETED"
    )
  );

  const displayedUserStories = userStories
    .filter(
      (story) =>
        !selectedProjectFilter ||
        (story.project && story.project.id === parseInt(selectedProjectFilter))
    )
    .sort(
      (a, b) =>
        (priorityOrder[a.priority] || 4) - (priorityOrder[b.priority] || 4)
    );

  return (
    <div className="app">
      <Header />
        <div className="new-userstory-container">
          <h2 className="userstory-title">Product Backlog</h2>
          <div className="new-userstory">
            <h3 className="new-userstory-title">New User Story</h3>
            <div className="new-userstory-form-group">
              <input
                type="text"
                placeholder="User Story Name"
                value={newUserStory.name}
                onChange={(e) =>
                  setNewUserStory({ ...newUserStory, name: e.target.value })
                }
                className="new-userstory-input-field"
                required
              />
              <input
                type="text"
                placeholder="Description"
                value={newUserStory.description}
                onChange={(e) =>
                  setNewUserStory({
                    ...newUserStory,
                    description: e.target.value,
                  })
                }
                className="new-userstory-input-field"
                required
              />
              <select
                value={newUserStory.priority}
                onChange={(e) =>
                  setNewUserStory({ ...newUserStory, priority: e.target.value })
                }
                className="new-userstory-input-field"
                required
              >
                <option value="">Select Priority</option>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
              <input
                type="date"
                value={newUserStory.endDate}
                onChange={(e) =>
                  setNewUserStory({ ...newUserStory, endDate: e.target.value })
                }
                className="new-userstory-input-field"
                required
              />
              <select
                value={newUserStory.projectId}
                onChange={(e) =>
                  setNewUserStory({
                    ...newUserStory,
                    projectId: e.target.value,
                  })
                }
                className="new-userstory-input-field"
                required
              >
                <option value="">Select Project</option>
                {projects.map((project) => (
                  <option key={project.id} value={project.id}>
                    {project.name}
                  </option>
                ))}
              </select>
              <button
                className="new-userstory-create-button"
                onClick={handleCreateUserStory}
              >
                Create
              </button>
            </div>
          </div>
          <div className="userstory-filter-container">
            <label className="userstory-projectFilter">
              Filter by Project:
            </label>
            <select
              id="projectFilter"
              value={selectedProjectFilter}
              onChange={(e) => setSelectedProjectFilter(e.target.value)}
              className="userstory-filter-dropdown"
            >
              <option value="">All Projects</option>
              {activeProjects.map((project) => (
                <option key={project.id} value={project.id}>
                  {project.name}
                </option>
              ))}
            </select>
          </div>
          <div className="new-userstory-container">
            <h3 class="userstory-list-title">All User Stories</h3>
            <ul className="userstory-responsive-table">
              <li className="userstory-table-header">
                <div className="userstory-col col-1">Name</div>
                <div className="userstory-col col-2">Description</div>
                <div className="userstory-col col-3">Priority</div>
                <div className="userstory-col col-4">Start Date</div>
                <div className="userstory-col col-5">End Date</div>
                <div className="userstory-col col-6">Project</div>
                <div className="userstory-col col-7">Status</div>
                <div className="userstory-col col-8">Actions</div>
              </li>
              <div class="userstory-container">
                {displayedUserStories.map((userStory) => (
                  <li
                    className="userstory-table-row"
                    key={userStory.id}
                    onClick={() => {
                      setSelectedUserStory(userStory);
                      setSelectedPopup(true);
                    }}
                  >
                    <div className="userstory-col col-1">
                      {userStory.name.length > 10
                        ? userStory.name.substring(0, 10) + "..."
                        : userStory.name}
                    </div>
                    <div className="userstory-col col-2">
                      {userStory.description.length > 20
                        ? userStory.description.substring(0, 20) + "..."
                        : userStory.description}
                    </div>
                    <div className="userstory-col col-3">
                      {userStory.priority.charAt(0).toUpperCase() +
                        userStory.priority.slice(1).toLowerCase()}
                    </div>
                    <div className="userstory-col col-4">
                      {formatDate(userStory.startDate)}
                    </div>
                    <div className="userstory-col col-5">
                      {formatDate(userStory.endDate)}
                    </div>
                    <div className="userstory-col col-6">
                      {userStory.project
                        ? userStory.project.name.length > 20
                          ? userStory.project.name.substring(0, 20) + "..."
                          : userStory.project.name
                        : "N/A"}
                    </div>
                    <div className="userstory-col col-7">{userStory.state}</div>
                    <div className="userstory-col col-8 action-buttons">
                      <button
                        className="userstory-edit-button"
                        onClick={(e) => handleEditUserStory(e, userStory)}
                      >
                        Edit
                      </button>
                      <button
                        className="userstory-drop-button"
                        onClick={(e) => {
                          e.stopPropagation();
                          handleDropUserStory(e, userStory.id);
                        }}
                      >
                        Drop
                      </button>
                    </div>
                  </li>
                ))}
              </div>
            </ul>
          </div>
        </div>

      <SelectedPopup trigger={selectedPopup} setTrigger={setSelectedPopup}>
        {selectedUserStory && (
          <div>
            <p>
              <strong>Name: </strong> {selectedUserStory.name}
            </p>
            <p>
              <strong>Description: </strong> {selectedUserStory.description}
            </p>
            <p>
              <strong>Priority: </strong>
              {selectedUserStory.priority.charAt(0).toUpperCase() +
                selectedUserStory.priority.slice(1).toLowerCase()}
            </p>
            <p>
              <strong>Start Date: </strong>{" "}
              {formatDate(selectedUserStory.startDate)}
            </p>
            <p>
              <strong>End Date: </strong>{" "}
              {formatDate(selectedUserStory.endDate)}
            </p>
            <p>
              <strong>Project: </strong> {selectedUserStory.project.name}
            </p>
            <p>
              <strong>Status: </strong> {selectedUserStory.state}
            </p>
          </div>
        )}
      </SelectedPopup>

      <SelectedPopup trigger={editPopup} setTrigger={setEditPopup}>
        {editedUserStory && (
          <div>
            <h3 className="userstory-edit-popup">Edit User Story</h3>
            <input
              type="text"
              className="userstory-popup-input"
              value={editedUserStory.name || ""}
              onChange={(e) =>
                setEditedUserStory({ ...editedUserStory, name: e.target.value })
              }
            />
            <input
              type="text"
              className="userstory-popup-input"
              value={editedUserStory.description || ""}
              onChange={(e) =>
                setEditedUserStory({
                  ...editedUserStory,
                  description: e.target.value,
                })
              }
            />
            <select
              value={editedUserStory.priority}
              onChange={(e) =>
                setEditedUserStory({
                  ...editedUserStory,
                  priority: e.target.value,
                })
              }
              className="userstory-popup-input"
            >
              <option value="">Select Priority</option>
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
            <input
              type="date"
              className="userstory-popup-input"
              value={editedUserStory.endDate || ""}
              onChange={(e) =>
                setEditedUserStory({
                  ...editedUserStory,
                  endDate: e.target.value,
                })
              }
            />
            <select
              value={editedUserStory.projectId}
              onChange={(e) =>
                setEditedUserStory({
                  ...editedUserStory,
                  projectId: e.target.value,
                })
              }
              className="userstory-popup-input"
              required
            >
              <option value="">Select Project</option>
              {projects.map((project) => (
                <option key={project.id} value={project.id}>
                  {project.name}
                </option>
              ))}
            </select>
            <div className="userstory-save-btn-container">
              <button
                className="userstory-save-btn"
                onClick={handleSaveChanges}
              >
                Save
              </button>
            </div>
          </div>
        )}
      </SelectedPopup>
    </div>
  );
}
