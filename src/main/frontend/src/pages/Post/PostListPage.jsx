import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/axiosInstance';
import '../../style/PostStyles.css';

const PostListPage = () => {
  const [posts, setPosts] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await api.get('/posts');
        setPosts(response.data);
      } catch (error) {
        alert('게시물 불러오기 실패');
      }
    };
    fetchPosts();
  }, []);

  return (
    <div className="post-container">
      <h2 className="post-title">📋 게시판</h2>
      <button className="post-btn" onClick={() => navigate('/posts/create')}>
        새 글 작성
      </button>
      <ul className="post-list">
        {posts.map((post) => (
          <li
            key={post.id}
            className="post-item"
            onClick={() => navigate(`/posts/${post.id}`)}
          >
            <h3>{post.title}</h3>
            <p>작성자: {post.author}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default PostListPage;
