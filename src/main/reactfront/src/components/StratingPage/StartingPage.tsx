import axios from 'axios';
import {useEffect, useState} from 'react';

interface Post {
    id: number;
    title: string;
    writer: string;
    view: number;
}

const StartingPage = () => {

    const [posts, setPosts] = useState<Post[]>([]);
    const [loading, setLoading] = useState<boolean>(false);
    const [currentPage, setCurrentPage] = useState(1);
    const [postsPerPage, setPostsPerPage] = useState(10);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const response = await axios.get<Post[]>('/post/all');
            setPosts(response.data);
            setLoading(false);
        };
        fetchData();
    }, []);

    console.log(posts);

    return (
        <div className=''>
            {loading ? (
                <div>Loading...</div>
            ) : (
                <table className="table table-hover">
                    <thead className="text-center">
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>조회수</th>
                    </tr>
                    </thead>
                    <tbody>
                    {posts.map((post, index) => (
                        <tr key={post.id}>
                            <td>{index + 1}</td>
                            <td>{post.title}</td>
                            <td>{post.writer}</td>
                            <td>{post.view}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}

export default StartingPage;